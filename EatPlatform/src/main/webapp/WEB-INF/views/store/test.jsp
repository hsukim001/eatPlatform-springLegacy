<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name='viewport' content='width=device-width, initial-scale=1'>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
<style>
.insert {
	padding: 20px 30px;
	display: block;
	width: 600px;
	margin: 5vh auto;
	height: 90vh;
	border: 1px solid #dbdbdb;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

.insert .file-list {
	height: 200px;
	overflow: auto;
	border: 1px solid #989898;
	padding: 10px;
}

.insert .file-list .filebox p {
	font-size: 14px;
	margin-top: 10px;
	display: inline-block;
}

.insert .file-list .filebox .delete i {
	color: #ff5353;
	margin-left: 5px;
}

#uploadBtn {
	display: none;
}

.uploadLabel {
  padding: 6px 25px;
  background-color:#FF6600;
  border-radius: 4px;
  color: white;
  cursor: pointer;
}

</style>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/store/ImageUpload.js"></script>
<title>Insert title here</title>
</head>
<body>
	<div class="insert">
		<form method="POST" onsubmit="return false;"
			enctype="multipart/form-data">
			<label class="uploadLabel" for="uploadBtn">
				업로드
			</label>
			<input id="uploadBtn" type="file" onchange="addFile(this);" multiple />
			<div class="file-list"></div>
		</form>
	</div>
</body>
</html>