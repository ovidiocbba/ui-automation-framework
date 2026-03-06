#!/usr/bin/env bash
set -e

echo "Generating index.html for Jenkins..."

OUTPUT="allure-report/index.html"
mkdir -p allure-report

# Create the output file and write the basic HTML structure
cat <<EOF > "$OUTPUT"
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>UI Automation Framework - Allure Reports</title>
<style>
body {
  font-family: Arial, sans-serif;
  text-align: center;
  background-color: #0f172a;
  color: white;
  margin-top: 60px;
}
h1 { margin-bottom: 40px; }
.container {
  display: flex;
  justify-content: center;
  gap: 30px;
  flex-wrap: wrap;
}
.card {
  background: #1e293b;
  padding: 30px;
  border-radius: 12px;
  width: 220px;
  transition: 0.3s;
}
.card:hover {
  background: #334155;
  transform: translateY(-5px);
}
a {
  color: #38bdf8;
  text-decoration: none;
  font-size: 18px;
  font-weight: bold;
}
</style>
</head>
<body>
<h1>Allure Test Reports</h1>
<div class="container">
EOF

# Loop for each browser and generate subreports
for browser in CHROME_HEADLESS FIREFOX_HEADLESS EDGE_HEADLESS; do
  report_dir="build-${browser}/allure-results"
  if [ -d "$report_dir" ] && [ "$(ls -A $report_dir)" ]; then
    # Generate a subreport for this browser
    allure generate "$report_dir" --clean -o "allure-report/allure-${browser}"

    # Add link to the index
    cat <<EOF >> "$OUTPUT"
  <div class="card">
    <a href="./allure-${browser}/index.html">${browser^} Report</a>
  </div>
EOF
  fi
done

cat <<EOF >> "$OUTPUT"
</div>
</body>
</html>
EOF

echo "index.html generated successfully."
