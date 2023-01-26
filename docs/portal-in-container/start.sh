#!/bin/bash

# 'gatsby build' crashes if has less than 9 Gb of RAM.
# This value is passed as an env var to change the default RAM setting.
# 
# Make sure containers have more than this value available, see https://docs.docker.com/config/daemon.
NODE_MEMORY_MB=11776  # 11.5 Gb

docker run \
    --name=dev_portal \
    --rm -it \
    -p 8000:8000 \
    -e "NODE_OPTIONS=--max-old-space-size=${NODE_MEMORY_MB}" \
    --mount type=bind,source="$(pwd)"/../portal/build/export,target=/app/packages/apps/devportal-documentation/documentation/tomtom-digital-cockpit,readonly \
    iviacrweu.azurecr.io/ivi/dev-portal:latest "$@"
