# TomTom Digital Cockpit SDK examples

## Introduction

This repository contains the source code of a template app and several example apps, built on
TomTom Digital Cockpit.

TomTom Digital Cockpit is a framework that delivers Android-based digital cockpits for automotive
environments. It includes an application platform to support the development of highly integrated
applications on top of Android Automotive. For more information, see the
[TomTom Digital Cockpit developer portal](https://developer.tomtom.com/tomtom-digital-cockpit/developers/introduction).

### Template application

The TomTom Digital Cockpit template application contains all of the off-the-shelf functionality implemented
by the TomTom Digital Cockpit platform. It is a fully working infotainment system, including navigation, a
media center, phone calling, messaging and much more.

The template application is explained in more detail on the
[Example Apps](https://developer.tomtom.com/tomtom-digital-cockpit/developers/platform-overview/example-apps#off-the-shelf-functionality)
page of the developer portal.

The source code is provided in the `template/` directory.

### Example applications

The example applications also contain all of the off-the-shelf functionality of the template
application, but on top of this they contain code demonstrating how to implement and integrate
specific features into the TomTom Digital Cockpit platform.

The example applications are explained on the
[Example apps](https://developer.tomtom.com/tomtom-digital-cockpit/developers/platform-overview/example-apps#example-apps)
page of the developer portal. Each example is explained in more detail in the
[Tutorials and Examples](https://developer.tomtom.com/tomtom-digital-cockpit/developers/tutorials-and-examples/overview)
section of the developer portal.

Each subdirectory of the `examples/` directory holds one or more example applications. Each
subdirectory holds a `README.md` file that briefly explains the purpose of the example application.

## Building and running

For the entire setup process, please consult the
[Getting Started](https://developer.tomtom.com/tomtom-digital-cockpit/getting-started/getting-started)
pages on the TomTom Digital Cockpit developer portal.

### TomTom Digital Cockpit platform libraries

The TomTom Digital Cockpit template app and example apps are delivered as source code, so that they can serve
as inspiration for developing your own application. These apps are built on the TomTom Digital Cockpit
platform, which is provided in binary format only.

The TomTom Digital Cockpit platform libraries can be downloaded from an on-line repository hosted by TomTom.
To use these libraries in your build process, you should request access to this repository, as
explained on the
[Getting Started](https://developer.tomtom.com/tomtom-digital-cockpit/getting-started/getting-started)
page of the developer portal, then follow the instructions on the
[Accessing the Artifact Repository](https://developer.tomtom.com/tomtom-digital-cockpit/getting-started/develop-with-the-sdk#configure-your-credentials)
page.

Once you have configfured your credentials, the build process will pull the TomTom Digital Cockpit platform
libraries from the repository and use them in the template app and the example apps.

## Directory structure

This section presents the subdirectories in this repository, and for each of them gives a brief
description of what can be found inside.

### `buildSrc/`

Code used by Gradle to sync the Gradle modules and build the app.

### `build-logic/`

Code listing the different repositories and module versions needed for building.

### `gradle/`

The Gradle wrapper used to sync the Gradle modules and build the app.

### `examples/`

The root directory for all the example apps. This folder can be removed if only the template
application (in the `template/` folder) is needed, see below.

### `template/`

This is a template application to create your own IVI application, it contains the minimum needed
to create a TomTom Digital Cockpit application with all the default functionality from the platform.

### `keystore/`

The keys needed to sign the application with the same keys as the TomTom Digital Cockpit reference hardware
build (see
[Flashing the reference hardware](https://developer.tomtom.com/tomtom-digital-cockpit/developers/integrating/flashing-the-reference-hardware))
for more details on the reference hardware).

### `permissions/`

Platform permissions needed on hardware for some of the functionality (related to media) to work.
This file will work with the template app, if another example app is used, please update the
package name at the top of the file. See
[Installing TomTom Digital Cockpit on Hardware](https://developer.tomtom.com/tomtom-digital-cockpit/developers/integrating/installing-tomtom-digital-cockpit-on-hardware)
for more information about the permissions.

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
