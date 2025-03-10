const fileList = document.querySelector('.file-list');

const observer = new MutationObserver(function(mutationsList) {
  mutationsList.forEach(function(mutation) {
    if (mutation.type === 'childList') {
      let fileBoxs = document.querySelectorAll('div.filebox *');
      let namesArray = Array.from(fileBoxs)
        .filter(element => element.classList.contains('name'))
        .map(element => element.textContent);

      console.log(namesArray);
    }
  });
});

const config = { childList: true, subtree: true };

observer.observe(fileList, config);

var fileNo = 0;
var filesArr = new Array();

function addFile(obj) {
    var maxFileCnt = 20;
    var attFileCnt = document.querySelectorAll('.filebox').length;
    var remainFileCnt = maxFileCnt - attFileCnt;
    var curFileCnt = obj.files.length;

    if (curFileCnt > remainFileCnt) {
        alert("이미지는 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
    } else {
        for (const file of obj.files) {
            if (validation(file)) {
                var reader = new FileReader();
                reader.onload = function () {
                    filesArr.push(file);
                };
                reader.readAsDataURL(file);

                let htmlData = '';
                htmlData += '<div id="file' + fileNo + '" class="filebox">';
                htmlData += '   <p class="name">' + file.name + '</p>';
                htmlData += '   <a class="delete" onclick="deleteFile(' + fileNo + ');"><i class="far fa-minus-square"></i></a>';
                htmlData += '</div>';
                $('.file-list').append(htmlData);

                fileNo++;
            } else {
                continue;
            }
        }
    }

    document.querySelector("input[type=file]").value = "";
}

function validation(obj){
    const fileTypes = ['application/pdf', 'image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif', 'application/haansofthwp', 'application/x-hwp'];
    if (obj.name.length > 100) {
        alert("비정상적인 파일명입니다.");
        return false;
    } else if (obj.size > (100 * 1024 * 1024)) {
        alert("업로드 가능한 최대 파일 용량은 100MB 입니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("비정상적인 파일 업로드 시도입니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("이미지 파일의 확장자를 확인해주세요");
        return false;
    } else {
        return true;
    }
}

function deleteFile(num) {
    document.querySelector("#file" + num).remove();
    filesArr[num].is_delete = true;
}

function submitForm() {
    var form = document.querySelector("form");
    var formData = new FormData(form);
    
    for (var i = 0; i < filesArr.length; i++) {
        if (!filesArr[i].is_delete) {
            formData.append("attach_file", filesArr[i]);
        }
    }

    $.ajax({
        method: 'POST',
        url: '/store/image',
        dataType: 'json',
        data: formData,
        async: true,
        timeout: 30000,
        cache: false,
        headers: {'cache-control': 'no-cache', 'pragma': 'no-cache'},
        success: function () {
            alert("파일업로드 성공");
        },
        error: function (xhr, desc, err) {
            alert('에러가 발생 하였습니다.');
        }
    })
}
