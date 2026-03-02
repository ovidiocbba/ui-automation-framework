#!/usr/bin/env bash
set -e

echo "Generating index.html..."

OUTPUT="public/index.html"

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

for dir in public/allure-*; do
  [ -d "$dir" ] || continue
  name=$(basename "$dir")
  browser=${name#allure-}

  cat <<EOF >> "$OUTPUT"
  <div class="card">
    <a href="./$name/index.html">${browser^} Report</a>
  </div>
EOF
done

cat <<EOF >> "$OUTPUT"
</div>
</body>
</html>
EOF

echo "index.html generated successfully."
