WatchMe for Android
=========

An Android app for keeping track of movies.

## Getting Started

	git clone git://github.com/johanbrook/watchme.git

## Dependencies

- Java 6 SE development environment
- Android SDK
- A (virtual) Android device
- Android SDK targets

### SDK targets

- Minimum SDK: 		**16**
- Target SDK:		**16**

## Building and installing

An Ant `build.xml` is included in the root directory which may be used for building the project, and used by ant to run tests, and various other tasks. The default output directory is bin in the project root.

To build the project in debug mode:

	$ ant clean debug

To build and install on a connected Android device:

	$ ant clean debug install

To uninstall the application from the device:

	$ ant uninstall

## Tests

Unit tests resides in a nested `WatchMeTest` project.

Run tests (be sure to navigate to the `WatchMeTest` project directory):
	
	$ ant test

## License

See the `LICENSE` file in project root.

## Team

- Lisa Stenberg
- Robin Andersson
- Mattias Henriksson
- Johan Brook

Built at Chalmers, 2012.