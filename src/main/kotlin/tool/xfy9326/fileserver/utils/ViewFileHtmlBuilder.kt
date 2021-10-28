package tool.xfy9326.fileserver.utils

import io.ktor.http.*
import kotlinx.html.*

@Suppress("JSUnusedLocalSymbols")
fun HTML.buildViewFileHtml(currentPath: String, files: List<String>, userName: String? = null) {
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
            unsafe {
                //language=JavaScript
                +"""
                        function selectFiles() {
                            document.getElementById("files").click();
                        }
                        
                        function upload(file) {
                            return fetch(window.location.href + file.name, {
                                method: "PUT"
                            }).then(response => {
                                if (response.ok) {
                                    return "Upload \'" + file.name + "\' success!";
                                } else {
                                    return response.text().then(msg => "Upload \'" + file.name + "\' failed! " + msg);
                                }
                            }).catch(error => {
                                console.error(error);
                                return "Upload \'" + file.name + "\' error!";
                            });
                        }
                        
                        document.addEventListener("DOMContentLoaded", function() {
                            document.getElementById("files").onchange = function(event) {
                                let files = event.target.files;
                                let results = [];
                                for (let i = 0; i < files.length; i++){
                                    results.push(upload(files[i]));
                                }
                                Promise.all(results).then(msgArray => {
                                    alert(msgArray.join("\r\n"));
                                    window.location.reload();
                                });
                            }
                        });
                    """.trimIndent()
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
            button {
                onClick = "selectFiles()"
                +"Upload"
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