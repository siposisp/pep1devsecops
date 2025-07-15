# fix-trivy-sarif.ps1
$json = Get-Content "trivy-source-report.sarif.json" -Raw | ConvertFrom-Json
$base = (Convert-Path ".").Replace('\','/')

foreach ($result in $json.runs[0].results) {
  foreach ($loc in $result.locations) {
    $uri = $loc.physicalLocation.artifactLocation.uri
    if ($uri -like 'file://*') {
      $clean = $uri -replace '^file://', ''
      $clean = $clean -replace [regex]::Escape($base + '/'), ''
      $loc.physicalLocation.artifactLocation.uri = $clean
    }
  }
}

$json | ConvertTo-Json -Depth 100 | Set-Content "trivy-source-report.sarif.json"
Write-Output "✅ Archivo SARIF corregido."
