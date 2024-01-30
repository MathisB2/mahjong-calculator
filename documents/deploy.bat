rmdir /s /q "Z:\public_html\sae"
xcopy /e /h /i "..\client" "Z:\public_html\sae"
start "" "http://iutbg-lamp.univ-lyon1.fr/%USERNAME%/public_html/sae/index.html"