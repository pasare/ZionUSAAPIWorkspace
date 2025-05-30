def FAILURE_STATUS = "Failed";
def SUCCESS_STATUS = "Succeeded";

def sendOffice365Message(appName, status, message, hash) {
  def SUCCESS_COLOR = "#00FF00"
  def FAILURE_COLOR = "#FF0000"
  def color = SUCCESS_COLOR
  if (status == "Failed") {
    color = FAILURE_COLOR
  }
  office365ConnectorSend webhookUrl: "${TEAMS_O365_WEB_HOOK}",
    color: "${color}",
    message: """- Branch: ${BRANCH_NAME}\n
- Message: ${message}\n
- Hash: ${hash}\n
        """,
    status: "${appName} Build ${status}"
}

def getCommandOutput(cmd) {
  if (isUnix()) {
    return sh(returnStdout:true , script: '#!/bin/sh -e\n' + cmd).trim()
  } else {
    stdout = bat(returnStdout:true , script: cmd).trim()
    result = stdout.readLines().drop(1).join(" ")
    return result
  }
}

def isManualBuild() {
  try {
    if (currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause) != null) {
      return true
    }
  } catch (Exception ignored) {
    return false
  }
  return false
}

def runCommand(cmd) {
  if (isUnix()) {
    sh(script: cmd)
  } else {
    bat(script: cmd)
  }
}

pipeline {
  agent any
  environment {
    GIT_COMMIT_HASH = getCommandOutput("git describe --always")
    GIT_COMMIT_MESSAGE = getCommandOutput("git log -n 1 --pretty=format:\"%%s\"")
  }
  options {
    timestamps()
  }
  stages {
    stage("Base") {
      when {
        anyOf {
          branch 'development'
          branch 'master'
          branch 'master-wlu'
          changeset "BaseApi/**"
          expression {
            isManualBuild()
          }
        }
      }
      options {
          timeout(30)
      }
      steps {
        dir("BaseApi") {
          load "Jenkinsfile"
        }
      }
    }
    stage("API Builds") {
      parallel {
        stage("Changes (Management)") {
          when {
            anyOf {
              branch 'development'
              branch 'master'
              branch 'master-wlu'
              changeset "BaseApi/**"
              changeset "ManagementApi/**"
              expression {
                isManualBuild()
              }
            }
          }
          options {
              timeout(30)
          }
          steps {
            dir("ManagementApi") {
              load "Jenkinsfile"
            }
          }
          post {
            success {
              sendOffice365Message("Management API", SUCCESS_STATUS, "${GIT_COMMIT_MESSAGE}", "${GIT_COMMIT_HASH}")
            }
            aborted {
              sendOffice365Message("Management API", FAILURE_STATUS, "${GIT_COMMIT_MESSAGE}", "${GIT_COMMIT_HASH}")
            }
          }
        }

        stage("Changes (Events)") {
          when {
            anyOf {
              branch 'development'
              branch 'master'
              branch 'master-wlu'
              changeset "BaseApi/**"
              changeset "EventsApi/**"
              expression {
                isManualBuild()
              }
            }
          }
          options {
              timeout(30)
          }
          steps {
            dir("EventsApi") {
              load "Jenkinsfile"
            }
          }
          post {
            success {
              sendOffice365Message("Events API", SUCCESS_STATUS, "${GIT_COMMIT_MESSAGE}", "${GIT_COMMIT_HASH}")
            }
            aborted {
              sendOffice365Message("Events API", FAILURE_STATUS, "${GIT_COMMIT_MESSAGE}", "${GIT_COMMIT_HASH}")
            }
          }
        }

        stage("Changes (Admin)") {
          when {
            anyOf {
              branch 'development'
              branch 'master'
              branch 'master-wlu'
              changeset "BaseApi/**"
              changeset "AdminApi/**"
              expression {
                isManualBuild()
              }
            }
          }
          options {
              timeout(30)
          }
          steps {
            dir("AdminApi") {
              load "Jenkinsfile"
            }
          }
          post {
            success {
              sendOffice365Message("Admin API", SUCCESS_STATUS, "${GIT_COMMIT_MESSAGE}", "${GIT_COMMIT_HASH}")
            }
            aborted {
              sendOffice365Message("Admin API", FAILURE_STATUS, "${GIT_COMMIT_MESSAGE}", "${GIT_COMMIT_HASH}")
            }
          }
        }

        stage("Changes (Bible Study)") {
          when {
            anyOf {
              branch 'development'
              branch 'master'
              branch 'master-wlu'
              changeset "BaseApi/**"
              changeset "BibleStudyApi/**"
              expression {
                isManualBuild()
              }
            }
          }
          options {
              timeout(30)
          }
          steps {
            dir("BibleStudyApi") {
              load "Jenkinsfile"
            }
          }
          post {
            success {
              sendOffice365Message("Bible Study API", SUCCESS_STATUS, "${GIT_COMMIT_MESSAGE}", "${GIT_COMMIT_HASH}")
            }
            aborted {
              sendOffice365Message("Bible Study API", FAILURE_STATUS, "${GIT_COMMIT_MESSAGE}", "${GIT_COMMIT_HASH}")
            }
          }
        }
      }
    }
  }
}
