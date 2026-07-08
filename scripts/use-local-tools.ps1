$WorkspaceRoot = Split-Path -Parent $PSScriptRoot
$LocalJava = Join-Path $WorkspaceRoot "tools\jdk-17"
$LocalMaven = Join-Path $WorkspaceRoot "tools\apache-maven-3.9.9"
$WingetNodeHome = Join-Path $env:LOCALAPPDATA "Microsoft\WinGet\Packages\OpenJS.NodeJS.LTS_Microsoft.Winget.Source_8wekyb3d8bbwe\node-v24.18.0-win-x64"
$DockerCliHome = Join-Path $env:ProgramFiles "Docker\Docker\resources\bin"

if (Test-Path -LiteralPath $LocalJava) {
    $env:JAVA_HOME = $LocalJava
    $env:Path = "$env:JAVA_HOME\bin;$env:Path"
}

if (Test-Path -LiteralPath $LocalMaven) {
    $env:MAVEN_HOME = $LocalMaven
    $env:Path = "$env:MAVEN_HOME\bin;$env:Path"
}

if (Test-Path -LiteralPath $WingetNodeHome) {
    $env:NODE_HOME = $WingetNodeHome
    $env:Path = "$env:NODE_HOME;$env:Path"
}

if (Test-Path -LiteralPath $DockerCliHome) {
    $env:Path = "$DockerCliHome;$env:Path"
}

if ($env:JAVA_HOME) {
    Write-Host "JAVA_HOME=$env:JAVA_HOME"
}
if ($env:MAVEN_HOME) {
    Write-Host "MAVEN_HOME=$env:MAVEN_HOME"
}
if ($env:NODE_HOME) {
    Write-Host "NODE_HOME=$env:NODE_HOME"
}

if (Get-Command java -ErrorAction SilentlyContinue) {
    java -version
}
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn -version
}
if (Get-Command node -ErrorAction SilentlyContinue) {
    node -v
    npm -v
}
if (Get-Command docker -ErrorAction SilentlyContinue) {
    docker --version
}
