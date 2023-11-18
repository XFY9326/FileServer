package tool.xfy9326.fileserver.utils

import io.ktor.http.*
import kotlinx.html.*

@Suppress("JSUnusedLocalSymbols")
fun HTML.buildViewFileHtml(currentPath: String, files: List<String>, userName: String? = null, allowAnonymousUpload: Boolean) {
    head {
        title {
            +"Path: $currentPath"
        }
        link {
            rel = "icon"
            type = "image/x-icon"
            href = "/favicon.ico"
        }
        meta {
            name = "viewport"
            content = "width=device-width, initial-scale=1.0"
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
                        
                        function humanFileSize(bytes, si=false, dp=1) {
                            const thresh = si ? 1000 : 1024;
                            if (Math.abs(bytes) < thresh) {
                                return bytes + ' B';
                            }
                            const units = si ? ['kB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'] : ['KiB', 'MiB', 'GiB', 'TiB', 'PiB', 'EiB', 'ZiB', 'YiB'];
                            let u = -1;
                            const r = 10**dp;
                            
                            do {
                                bytes /= thresh;
                                ++u;
                            } while (Math.round(Math.abs(bytes) * r) / r >= thresh && u < units.length - 1);
                            return bytes.toFixed(dp) + ' ' + units[u];
                        }
                        
                        function upload(files) {
                            let uploadBtn = document.getElementById("upload");
                            let formData = new FormData();
                            for(let i = 0; i < files.length; i++){
                                formData.append(files[i].name, files[i]);
                            }
                            let xhr = new XMLHttpRequest();
                            xhr.onloadstart = function () {
                                uploadBtn.disabled = true;
                                uploadBtn.innerText = "Uploading";
                            }
                            xhr.onloadend = function() {
                                if (xhr.status === 200) {
                                    window.location.reload();
                                } else {
                                    alert(xhr.response);
                                }
                                uploadBtn.disabled = false;
                                uploadBtn.innerText = "Upload";
                            };
                            xhr.upload.onprogress = function (event) {
                                if(event.lengthComputable) {
                                    let uploadPercent = Math.floor(event.loaded * 100 / event.total);
                                    uploadBtn.innerText = "Uploaded: " + uploadPercent + "% （" + humanFileSize(event.loaded) + "/" + humanFileSize(event.total) + ")";
                                }
                            };
                            xhr.open("POST", window.location.href);
                            xhr.send(formData);
                        }
                        
                        document.addEventListener("DOMContentLoaded", function() {
                            document.getElementById("files").onchange = function(event) {
                                upload(event.target.files)
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
                    id = "upload"
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