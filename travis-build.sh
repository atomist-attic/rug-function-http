#!/bin/bash
# build, test, and publish boot projects on Travis CI

set -o pipefail

declare Pkg=travis-build-boot
declare Version=0.1.0

function msg() {
    echo "$Pkg: $*"
}

function err() {
    msg "$*" 1>&2
}

function install(){
    if ! wget https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh -O boot; then
        err "unable to download boot install script"
        return 1
    fi
    if ! chmod 766 ./boot; then
        err "unable to chmod boot"
        return 1
    fi
    if ! ./boot -u; then
        err "unable to update to latest boot"
        return 1
    fi
}

function main() {
    msg "branch is ${TRAVIS_BRANCH}"

    local project_version
    if [[ $TRAVIS_TAG =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
        project_version="$TRAVIS_TAG"
    else
        project_version=$(./boot -q print-version | cut -d '-' -f1)-$(date -u '+%Y%m%d%H%M%S')
        if [[ $? != 0 || ! $project_version ]]; then
            err "failed to parse project version"
            return 1
        fi
    fi
    msg "Project version: $project_version"

    ./boot midje

    if [[ $TRAVIS_PULL_REQUEST != false ]]; then
        msg "not publishing or tagging pull request"
        return 0
    fi

    if [[ $TRAVIS_BRANCH == master || $TRAVIS_TAG =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
        msg "version is $project_version"

        if ! gpg --allow-secret-key-import --import atomist_sec.gpg; then
           err "Error importing gpg keys"
           return 1
        fi

        local deploy_args
        if [[ $project_version =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
            deploy_args="-r release"
        fi

        if ! ./boot deploy -v $project_version $deploy_args; then
            err "boot deploy failed"
            return 1
        fi

        if ! git config --global user.email "travis-ci@atomist.com"; then
            err "failed to set git user email"
            return 1
        fi
        if ! git config --global user.name "Travis CI"; then
            err "failed to set git user name"
            return 1
        fi
        local git_tag=$project_version+travis$TRAVIS_BUILD_NUMBER
        if ! git tag "$git_tag" -m "Generated tag from TravisCI build $TRAVIS_BUILD_NUMBER"; then
            err "failed to create git tag: $git_tag"
            return 1
        fi
        if ! git push --quiet --tags "https://$GITHUB_TOKEN@github.com/$TRAVIS_REPO_SLUG" > /dev/null 2>&1; then
            err "failed to push git tags"
            return 1
        fi
    fi
}

install "$@" || exit 1
main "$@" || exit 1
exit 0
