package tool.xfy9326.fileserver.utils

import io.ktor.http.*
import kotlinx.html.*
import tool.xfy9326.fileserver.beans.IConfig

@Suppress("JSUnusedLocalSymbols")
fun HTML.buildViewFileHtml(currentPath: String, files: List<String>, userName: String? = null, allowAnonymousUpload: Boolean) {
    head {
        title {
            +"Path: $currentPath"
        }
        script {
            if (userName != null) {
                unsafe {
                    //language=JavaScript
                    +"""
                        function logout() {
                            fetch("/logout").then(function() {
                                window.location.reload()
                            });
                        }
                        
                        
                    """.trimIndent()
                }
            }
            if (allowAnonymousUpload || userName != null) {
                unsafe {
                    //language=JavaScript
                    +"""
                        function selectFiles() {
                            document.getElementById("files").click();
                        }
                        
                        function upload(files) {
                            let formData = new FormData();
                            for(let i = 0; i < files.length; i++){
                                formData.append(files[i].name, files[i]);
                            }
                            return fetch(window.location.href, {
                                method: "POST",
                                body: formData
                            }).then(response => response.text()).catch(error => {
                                console.error(error);
                                return "Upload error!";
                            });
                        }
                        
                        document.addEventListener("DOMContentLoaded", function() {
                            document.getElementById("files").onchange = function(event) {
                                let files = event.target.files;
                                upload(files).then(msg => {
                                    alert(msg);
                                    window.location.reload();
                                });
                            }
                        });
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
            +"Files: ${Typography.nbsp}${Typography.nbsp}${Typography.nbsp}${Typography.nbsp}"
            if (allowAnonymousUpload || userName != null) {
                button {
                    onClick = "selectFiles()"
                    +"Upload"
                }
            }
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
        input {
            id = "files"
            type = InputType.file
            multiple = true
            style = "display: none;"
        }
    }
}