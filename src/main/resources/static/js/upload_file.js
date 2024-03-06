//upload_file.js
const selectFileButton = document.getElementById('selectFile');
const uploadFileButton = document.getElementById('uploadFile');
const MAX_FILE_SIZE_MB = 20; // как то надо дублировать это значение в application.properties
/*******************************************************************************
 *******************     Блок отправки файла на сервер      ********************
 *******************************************************************************/
function uploadFile(file) {
    console.log("uploadFile() " + file.name);
    if (!file) {
        alert("Please select a file to upload");
        return;
    }

    if (file.size > MAX_FILE_SIZE_MB * 1024 * 1024) {
        selectFileButton.value = null;
        alert(`Файл слишком большой. Максимальный размер файла: ${MAX_FILE_SIZE_MB} MB`);
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    fetch("/api/files", {
        method: "POST",
        headers: {
            // Какие заголовки будем слать?
        },
        body: formData,
    })
        .then(response => {
            console.log("post response")
            if(!response.ok) {
                selectFileButton.value = null;
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            selectFileButton.value = null;
            console.log("Received data:", data);
            displayFileList(data);
        })
        .catch((error) => {
            selectFileButton.value = null;
            console.error("Error uploading file:", error);
            if (error instanceof TypeError && error.message.includes("NetworkError")) {
                alert('Network error. Please check your internet connection.');
            } else {
                // Send detailed error information to the server
                fetch("/api/error", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ error: error.stack }),
                });
                alert('An error occurred during the request. Please contact support.');
            }
        });

}
startPageLoad();
function startPageLoad() {
    fetch("/api/files")
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json(); // Парсим JSON ответ
        })
        .then(data => {
            console.log("Received data on page load:", data);
            // Вызываем функцию для отображения файлов на странице
            displayFileList(data);
        })
        .catch((error) => {
            console.error("Error loading files on page load:", error);
        });
}

function displayFileList(fileList) {
    const filesContainer = document.getElementById('filesContainer');
    filesContainer.innerHTML = ""; // Очистить предыдущий список

    fileList.forEach(fileData => {
        const listItem = document.createElement('li');
        listItem.textContent = `${fileData.idFileData} - ${fileData.originalName} - ${fileData.createDate}`;
        filesContainer.appendChild(listItem);
    });
}

uploadFileButton.addEventListener('click', () => {
    const selectedFile = selectFileButton.files[0];
    uploadFile(selectedFile);
});



