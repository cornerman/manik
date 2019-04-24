rm server/src/main/resources/application.conf
mv server/src/main/resources/application.conf-github server/src/main/resources/application.conf

git config --global user.email "jctaurys@gmail.com"
git config --global user.name "elyphas"

git init
git remote add origin https://github.com/elyphas/manik.git
#echo "# manik" >> README.md
git add README.md
git commit -m "first commit"
git push -u origin master
