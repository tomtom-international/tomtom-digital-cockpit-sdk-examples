# This Dockerfile builds a container that can be used to test the devportal locally,
# without installing all necessary tools.
# Steps are taken from
# https://github.com/tomtom-internal/drumkit-monorepo/tree/develop/packages/apps/devportal-documentation
#
# NOTE: Use `build.sh` to build the container, as it clones `drumkit-monorepo` before building.
#
FROM node:18
WORKDIR /app

COPY drumkit-monorepo .
RUN npm i --global lerna gatsby-cli
RUN corepack enable && corepack prepare pnpm@7.12.2 --activate
RUN lerna clean --y
RUN pnpm install

RUN echo "cd ./packages/apps/devportal-documentation; pnpm run develop" > ./start.sh
RUN chmod +x ./start.sh
CMD /app/start.sh
