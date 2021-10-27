package tool.xfy9326.fileserver.utils

import io.ktor.http.*
import kotlinx.html.*

fun HTML.buildViewFileHtml(currentPath: String, files: List<String>, userName: String? = null) {
    head {
        title {
            +"Path: $currentPath"
        }
        if (userName != null) {
            script {
                unsafe {
                    +"""
                        function logout() {
                            let request = new XMLHttpRequest();
                            request.open("GET", window.location.host, true);
                            request.setRequestHeader("Authorization", null);
                            request.onload = function () {
                            window.location = location.protocol + "//" + window.location.host;
                            };
                            request.send();
                        }
                    """.trimIndent()
                }
            }
        }
    }
    body {
        h2 {
            +"Path: $currentPath"
        }
        if (userName != null) {
            p {
                +"Current user: $userName ${Typography.nbsp}${Typography.nbsp}${Typography.nbsp}${Typography.nbsp}"
                button {
                    onClick = "logout()"
                    +"Logout"
                }
            }
        }
        h3 {
            +"Files:"
        }
        ul {
            if (currentPath != "/") {
                li {
                    a {
                        href = "../"
                        +"../"
                    }
                }
            }
            for (file in files) {
                li {
                    a {
                        href = file.encodeURLPath()
                        +file
                    }
                }
            }
        }
        p {
            +"Total: ${files.size} files"
        }
    }
}