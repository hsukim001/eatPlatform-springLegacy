<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 업로드 페이지</title>
</head>
<body>
   <h1>파일 업로드 폼</h1>
   <h2>단일 파일 업로드</h2>
   <form action="upload" method="post" enctype="multipart/form-data">
      <input type="file" name="file">
      <input type="submit" value="업로드">
   </form>
   
   <h2>다중 파일 업로드</h2>
   <form action="uploads" method="post" enctype="multipart/form-data">
      <input type="file" name="files" multiple="multiple"><br>
      <input type="submit" value="업로드">
   </form>
   
</body>
</html>