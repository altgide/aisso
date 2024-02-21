<!DOCTYPE html>
<html>
<head>
    <title>CSV File Upload</title>
</head>
<body>
    <h2>Upload CSV File</h2>
    <form action="UploadUser" method="post" enctype="multipart/form-data">
        Select CSV file to upload:
        <input type="file" name="file" id="file">
        <input type="submit" value="Upload" name="submit">
    </form>
</body>
</html>