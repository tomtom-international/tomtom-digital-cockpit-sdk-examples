# Alexa example application

## Introduction

This example app shows how to use the Alexa feature that comes with the TomTom Digital Cockpit
Platform. The
[TomTom Digital Cockpit Developer Portal](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/introduction)
explains how this app
[creates a custom Alexa Handler service](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/tutorials-and-examples/voice-personal-assistant/create-a-custom-alexa-handler-service#how-to-create-a-custom-alexa-handler-service).

By default, this app is excluded from the build, because it requires Alexa libraries that are not
publicly available. You must
[set a Gradle property](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/tutorials-and-examples/voice-personal-assistant/create-a-custom-alexa-handler-service#example-app)
to build this app.

## Alexa Car Control

One of the Alexa Auto features that will most likely need customizations for your own vehicle
requirements, is Car Control. The Car Control module enables an OEM to build a custom experience that
allows users to use Alexa to voice-control vehicle features.

Subdirectory `customcarcontrolhandler` holds example code showing how to define custom Alexa
CarControl endpoints and assets. For details see the
[Customize the Alexa Car Control Feature](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/tutorials-and-examples/voice-personal-assistant/customize-the-alexa-car-control-feature)
portal page.

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
