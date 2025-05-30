Start-Transcript -Path "C:\scripts\Logs\APIBuildLog.txt" -Append

$checkAdmin = Test-Path "C:\inetpub\wwwroot\FTP\buildAdminAPIFiles"
$checkBibleStudy = Test-Path "C:\inetpub\wwwroot\FTP\buildBiblestudyAPIFiles"
$checkManagement = Test-Path "C:\inetpub\wwwroot\FTP\buildManagementAPIFiles"
$checkWLUManagement = Test-Path "C:\inetpub\wwwroot\FTP\buildWLUManagementAPIFiles"
$checkEvents = Test-Path "C:\inetpub\wwwroot\FTP\buildEventsAPIFiles"
$checkWLUEvents = Test-Path "C:\inetpub\wwwroot\FTP\buildWLUEventsAPIFiles"

if ($checkAdmin) {
    try {
        C:/inetpub/wwwroot/API/ZionUSAAdminApi/AdminApi.exe stop
        Start-Sleep -s 3
        Copy-Item "C:\inetpub\wwwroot\FTP\buildAdminAPIFiles\*" -Destination "C:/inetpub/wwwroot/API/ZionUSAAdminApi" -Recurse -force
        Start-Sleep -s 3
        C:/inetpub/wwwroot/API/ZionUSAAdminApi/AdminApi.exe start
        Start-Sleep -s 120
        Remove-Item –path "C:\inetpub\wwwroot\FTP\buildAdminAPIFiles" –recurse }
    catch {
        Write-Host "An error occurred:"
        Write-Host $_
        Write-Host $_.ScriptStackTrace
    }
}

if ($checkBibleStudy) {
    try {
        C:/inetpub/wwwroot/API/ZionUSABibleStudyApi/BibleStudyApi.exe stop
        Start-Sleep -s 3
        Copy-Item "C:\inetpub\wwwroot\FTP\buildBibleStudyAPIFiles\*" -Destination "C:/inetpub/wwwroot/API/ZionUSABibleStudyApi" -Recurse -force
        Start-Sleep -s 3
        C:/inetpub/wwwroot/API/ZionUSABibleStudyApi/BibleStudyApi.exe start
        Start-Sleep -s 120
        Remove-Item –path "C:\inetpub\wwwroot\FTP\buildBibleStudyAPIFiles" –recurse }
    catch {
        Write-Host "An error occurred:"
        Write-Host $_
        Write-Host $_.ScriptStackTrace
    }
}

if ($checkManagement) {
    try {
        C:/inetpub/wwwroot/API/ManagementApi/ManagementApi.exe stop
        Start-Sleep -s 3
        Copy-Item "C:\inetpub\wwwroot\FTP\buildManagementAPIFiles\*" -Destination "C:/inetpub/wwwroot/API/ManagementApi" -Recurse -force
        Start-Sleep -s 3
        C:/inetpub/wwwroot/API/ManagementApi/ManagementApi.exe start
        Start-Sleep -s 120
        Remove-Item –path "C:\inetpub\wwwroot\FTP\buildManagementAPIFiles" –recurse }
    catch {
        Write-Host "An error occurred:"
        Write-Host $_
        Write-Host $_.ScriptStackTrace
    }
}

if ($checkWLUManagement) {
    try {
        C:/inetpub/wwwroot/API/ManagementApi/ManagementApi.exe stop
        Start-Sleep -s 3
        Copy-Item "C:\inetpub\wwwroot\FTP\buildWLUManagementAPIFiles\*" -Destination "C:/inetpub/wwwroot/API/ManagementApi" -Recurse -force
        Start-Sleep -s 3
        C:/inetpub/wwwroot/API/ManagementApi/ManagementApi.exe start
        Start-Sleep -s 120
        Remove-Item –path "C:\inetpub\wwwroot\FTP\buildWLUManagementAPIFiles" –recurse }
    catch {
        Write-Host "An error occurred:"
        Write-Host $_
        Write-Host $_.ScriptStackTrace
    }
    C:/inetpub/wwwroot/API/ManagementApi/ManagementApi.exe stop
    Copy-Item "C:\inetpub\wwwroot\FTP\buildWLUManagementAPIFiles\*" -Destination "C:/inetpub/wwwroot/API/ManagementApi" -Recurse -force
    C:/inetpub/wwwroot/API/ManagementApi/ManagementApi.exe start
    Remove-Item –path "C:\inetpub\wwwroot\FTP\buildWLUManagementAPIFiles" –recurse
}

if ($checkEvents) {
    try {
        C:/inetpub/wwwroot/API/EventsApi/EventsApi.exe stop
        Start-Sleep -s 3
        Copy-Item "C:\inetpub\wwwroot\FTP\buildEventsAPIFiles\*" -Destination "C:/inetpub/wwwroot/API/EventsApi" -Recurse -force
        Start-Sleep -s 3
        C:/inetpub/wwwroot/API/EventsApi/EventsApi.exe start
        Start-Sleep -s 120
        Remove-Item –path "C:\inetpub\wwwroot\FTP\buildEventsAPIFiles" –recurse }
    catch {
        Write-Host "An error occurred:"
        Write-Host $_
        Write-Host $_.ScriptStackTrace
    }
}

if ($checkWLUEvents) {
    try {
        C:/inetpub/wwwroot/API/EventsApi/EventsApi.exe stop
        Start-Sleep -s 3
        Copy-Item "C:\inetpub\wwwroot\FTP\buildWLUEventsAPIFiles\*" -Destination "C:/inetpub/wwwroot/API/EventsApi" -Recurse -force
        Start-Sleep -s 3
        C:/inetpub/wwwroot/API/EventsApi/EventsApi.exe start
        Start-Sleep -s 120
        Remove-Item –path "C:\inetpub\wwwroot\FTP\buildWLUEventsAPIFiles" –recurse }
    catch {
        Write-Host "An error occurred:"
        Write-Host $_
        Write-Host $_.ScriptStackTrace
    }
}

Stop-Transcript
