<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/Login" method="post">
        <label for="username">UserName: </label>
        <input id="username" name="username" type="text" required><br />

        <label for="password">Password: </label>
        <input id="password" name="password" type="password" required><br />

        <input type="submit" value="Login">
    </form>
</body>
</html>