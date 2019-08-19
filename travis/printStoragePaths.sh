NOW=$(date +"%y-%m-%d")
GOOGLE_STORAGE_JOB_FOLDER="cicd-246518-tests/integration/${NOW}/${TRAVIS_COMMIT}/${DB_TYPE}"

BASE_GOOGLE_URL="https://storage.googleapis.com/"
reportsIndexURL="${BASE_GOOGLE_URL}${GOOGLE_STORAGE_JOB_FOLDER}/reports/html/integrationTest/index.html"
logURL="${BASE_GOOGLE_URL}${GOOGLE_STORAGE_JOB_FOLDER}/logs/dotcms.log"

echo ""
echo -e "\e[34m==========================================================================================================================\e[0m"
echo -e "\e[34m==========================================================================================================================\e[0m"
echo -e "\e[1;34m                                                REPORTING\e[0m"
echo
echo -e "\e[38;5;214m   >> ${reportsIndexURL}\e[0m"
echo
echo -e "\e[38;5;214m   >> ${logURL}\e[0m"
echo
if [ "$TRAVIS_PULL_REQUEST" != "false" ];
then
  echo "  GITHUB pull request: [https://github.com/dotCMS/core/pull/${TRAVIS_PULL_REQUEST}]"
fi
echo -e "\e[34m==========================================================================================================================\e[0m"
echo -e "\e[34m==========================================================================================================================\e[0m"
echo ""