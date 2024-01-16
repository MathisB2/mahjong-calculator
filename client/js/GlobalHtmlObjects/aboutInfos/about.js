import {projectConfig} from "../../config.js";

const versionName = document.getElementById("aboutVersionName");
const devList = document.getElementById("aboutDevList");
const languages = document.getElementById("aboutLanguages");
const sourceCodeLink = document.getElementById("aboutSourceCodeLink");
const issueLink = document.getElementById("aboutIssueLink");


export async function startAbout(){
    if(!(versionName && devList && sourceCodeLink && issueLink)) return

    versionName.innerText = projectConfig.name+" V"+projectConfig.version;
    devList.innerText = projectConfig.devs;
    languages.innerText = projectConfig.languages;
    sourceCodeLink.setAttribute("href",projectConfig.gitLink);
    issueLink.setAttribute("href",projectConfig.gitIssueLink);
}
