# build script fuer die todo app
# benoetigt nur java (javac + jar), kein maven

$SRC = "src"
$OUT = "out"
$JAR = "todoapp.jar"
$MAIN = "de.dhsn.todoapp.App"

# out-verzeichnis leeren / anlegen
if (Test-Path $OUT) { Remove-Item -Recurse -Force $OUT }
New-Item -ItemType Directory -Path $OUT | Out-Null

# alle java-dateien sammeln
$sources = Get-ChildItem -Path $SRC -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
[System.IO.File]::WriteAllLines("$PWD\sources.txt", $sources)

Write-Host "Kompiliere $($sources.Count) Dateien..."
javac -encoding UTF-8 -d $OUT "@sources.txt"

if ($LASTEXITCODE -ne 0) {
    Write-Host "Kompilierung fehlgeschlagen." -ForegroundColor Red
    exit 1
}

Write-Host "Erstelle $JAR..."
jar cfe $JAR $MAIN -C $OUT .

Remove-Item "sources.txt"

Write-Host "Fertig. Starten mit: java -jar $JAR" -ForegroundColor Green
