#!/bin/bash

git clone \
    --depth 1 \
    --branch develop \
    --single-branch \
    --no-tags \
    git@github.com-tomtom:tomtom-internal/drumkit-monorepo.git

docker build \
    --pull \
    --rm \
    -f portal_in_container.Dockerfile \
    -t iviacrweu.azurecr.io/ivi/dev-portal-check:latest \
    .

rm -rf drumkit-monorepo
