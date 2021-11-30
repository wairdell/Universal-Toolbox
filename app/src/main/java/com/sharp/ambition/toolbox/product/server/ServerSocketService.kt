package com.sharp.ambition.toolbox.product.server

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import okio.BufferedSource
import okio.buffer
import okio.sink
import okio.source
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

/**
 *    author : fengqiao
 *    date   : 2021/11/29 16:51
 *    desc   :
 */
class ServerSocketService : Service() {

    companion object {
        private val TAG = "Server"
        private val executorService = Executors.newCachedThreadPool()
    }

    private lateinit var acceptRunnable: AcceptRunnable

    override fun onCreate() {
        super.onCreate()
        acceptRunnable = AcceptRunnable(this)
        Thread(acceptRunnable).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        acceptRunnable.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private class AcceptRunnable(val context: Context) : Runnable {

        private var isRunning: Boolean = true

        override fun run() {
            val serverSocket = ServerSocket(9888)

            while (isRunning) {
                val socket = serverSocket.accept()
                Log.i(TAG, "收到新连接" + socket.inetAddress.hostAddress)
                executorService.execute(ProtocolRunnable(context, socket))
            }
            serverSocket.close()
        }

        fun stop() {
            isRunning = false
        }
    }

    private class ProtocolRunnable(val context: Context, val socket: Socket) : Runnable {

        private fun getContentType(suffix: String): String {
            when (suffix) {
                "png", "jpeg" -> return "image/${suffix}"
                "mp4" -> return "video/im4"
                "html", ".htx", ".htm" -> return "text/html; charset=UTF-8"
            }
            return "application/"
        }

        override fun run() {
            try {
                while (true) {
                    val servletRequest = read(socket.source().buffer())
                    if (servletRequest.requestURI.startsWith("/assets/")) {
                        val fileName = servletRequest.requestURI.substring("/assets/".length)
                        val bytes = context.assets.open(fileName).source().buffer().readByteArray()
                        write(bytes, getContentType(fileName.split(".").last()))
                    } else if(servletRequest.requestURI.startsWith("/api/comments")) {
                        if(servletRequest.method == "GET") {
                            write("[{\"author\":\"Pete Hunt\",\"text\":\"This is one comment\"},{\"author\":\"Jordan Walke\",\"text\":\"This is *another* comment\"}]".toByteArray(), "application/json")
                        } else {
                            write("[{\"author\":\"Pete Hunt\",\"text\":\"This is one comment\"},{\"author\":\"Jordan Walke\",\"text\":\"This is *another* comment\"},{\"author\":\"ZHOUWENCHAO\",\"text\":\"ADD SUCCESS\"}]".toByteArray(), "application/json")
                        }
                    } else {
                        write("Hello World".toByteArray(), "text/plain")
                    }
                    if (servletRequest.getHeader("Connection") != "keep-alive") {
                        return
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                socket.close()
            }
        }
        
        private fun read(buffer: BufferedSource): HttpServletRequest {
            var contentLength = 0L
            val httpServletRequest = HttpServletRequest()
            val readUtf8Line = buffer.readUtf8Line() ?: throw ParseException()
            val tag = "${TAG}(${socket.inetAddress.hostAddress})"
            Log.i(tag, "---------------------- 请求行 ---------------------------")
            Log.i(tag, readUtf8Line ?: "")
            val lineSplitList = readUtf8Line.split(" ")
            httpServletRequest.method = lineSplitList[0]
            httpServletRequest.requestURI = lineSplitList[1]
            Log.i(tag, "---------------------- 请求头 ---------------------------")
            var line : String? = ""
            while (buffer.readUtf8Line().also { line = it }?.isNotBlank() == true) {
                Log.i(tag, line ?:"")
                line?.let {
                    if(it.startsWith("Content-Length")) {
                        val lengthString = line!!.replaceFirst("Content-Length: ", "")
                        contentLength = lengthString.toLong()
                    }
                    val headerSplit = it.split(": ", limit = 2)
                    httpServletRequest.headers[headerSplit[0]] = headerSplit[1]
                }
            }
            if(contentLength > 0) {
                val readByteArray = buffer.readByteArray(contentLength)
                Log.i(tag, "---------------------- 请求体 ---------------------------")
                Log.i(tag, String(readByteArray))
            }
            return httpServletRequest
        }

        private fun write(content: ByteArray, contentType: String) {
            socket.sink().buffer().writeUtf8("HTTP/1.1 200 OK\r\n")
                .writeUtf8("Content-Type: ${contentType}\r\n")
                .writeUtf8("Content-Length: ${content.size}\r\n")
                .writeUtf8("\r\n")
                .write(content)
                .flush()
        }

    }

}