# Frontend

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 19.2.12.

## Development server

Make sure proxy file is pointing to right backend server. Check Development
Proxy file [proxy.dev.conf.json](src/proxy.dev.conf.json). It will route all ```/api``` request to backend server
configure in this file.

```
{
  "/api/**": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "debug"
  }
}
```

To start a local development server, run below command.
Default configuration is development. Check [angular.json](angular.json)

Run below command from ```src/main/frontend```

```bash
ng serve
```

If you chose not to update angular.json, you can pass the proxy configuration file directly in the command:
Check Documentation
[Proxy Config Backend](https://angular.dev/tools/cli/serve#proxying-to-a-backend-server) and
[CLI Options](https://angular.dev/cli/serve)

```bash
ng serve --proxy-config src/proxy.dev.conf.json
```

for production build to run in local

```bash
ng serve --configuration production 
```

this will use [proxy.prod.conf.json](src/proxy.prod.conf.json)

OR run from command line via

```bash
ng serve --proxy-config src/proxy.dev.conf.json
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
