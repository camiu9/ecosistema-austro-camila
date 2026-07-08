$WorkspaceRoot = Split-Path -Parent $PSScriptRoot
$env:JAVA_HOME = Join-Path $WorkspaceRoot "tools\jdk-17"
$env:MAVEN_HOME = Join-Path $WorkspaceRoot "tools\apache-maven-3.9.9"
$WingetNodeHome = Join-Path $env:LOCALAPPDATA "Microsoft\WinGet\Packages\OpenJS.NodeJS.LTS_Microsoft.Winget.Source_8wekyb3d8bbwe\node-v24.18.0-win-x64"
$DockerCliHome = Join-Path $env:ProgramFiles "Docker\Docker\resources\bin"

if (Test-Path -LiteralPath $WingetNodeHome) {
    $env:NODE_HOME = $WingetNodeHome
    $env:Path = "$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:NODE_HOME;$env:Path"
} else {
    $env:Path = "$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:Path"
}

if (Test-Path -LiteralPath $DockerCliHome) {
    $env:Path = "$DockerCliHome;$env:Path"
}

Write-Host "JAVA_HOME=$env:JAVA_HOME"
Write-Host "MAVEN_HOME=$env:MAVEN_HOME"
if ($env:NODE_HOME) {
    Write-Host "NODE_HOME=$env:NODE_HOME"
}
java -version
mvn -version
if ($env:NODE_HOME) {
    node -v
    npm -v
}
if (Get-Command docker -ErrorAction SilentlyContinue) {
    docker --version
}
