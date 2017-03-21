var emsApp = angular.module('emsApp', [
    'ngRoute',
    'ng-fusioncharts',
    'ngResource',
    'ngStorage',
    'emsControllers',
    'emsServices',
    'satellizer',
    'ngMessages',
    'ui.router',
    'ngAnimate',
    'toastr',
    'ngFileUpload',
    'angularUtils.directives.dirPagination',
    'angular-loading-bar',
   'ui.bootstrap.datetimepicker',
   'uiGmapgoogle-maps'
   ]);

   emsApp.value("userProfile","shaheedah");

emsApp.config(['$routeProvider', '$authProvider','$stateProvider','$urlRouterProvider',
           function($routeProvider, $authProvider, $stateProvider , $urlRouterProvider){

         $stateProvider
                 .state('index', {
                 url: '/',
                 templateUrl: '/assets/partials/login.html',
                 controller: 'LoginController',
                 resolve: { skipIfLoggedIn: skipIfLoggedIn }
                  })
                  .state('login', {
                  url: '/login',
                  templateUrl: '/assets/partials/login.html',
                  controller: 'LoginController',
                  resolve: { skipIfLoggedIn: skipIfLoggedIn }
                  })
                  .state('home', {
                  url: '/home',
                  templateUrl: '/assets/partials/home.html',
                  controller: 'IndexController',
                  resolve: { loginRequired: loginRequired }
                  })
                  .state('home.map', {
                  url: '/map',
                  templateUrl: '/assets/partials/map.html',
                  controller: 'MapController',
                  resolve: { loginRequired: loginRequired }
                  })
                  .state('home.ocad', {
                  url: '/ocad/home',
                  templateUrl: '/assets/partials/ocad/main.html',
                  controller: 'OCADMainController',
                  resolve: { loginRequired: loginRequired }
                  })
                  .state('home.about', {
                  url: '/about',
                  templateUrl: '/assets/partials/about.html',
                  controller: 'AboutController',
                  resolve: { loginRequired: loginRequired }
                  })
                  .state('home.correlation', {
                  url: '/correlation',
                  templateUrl: '/assets/partials/ocad/correlation-table.html',
                  controller: 'CorrelationController',
                  resolve: { loginRequired: loginRequired }
                  })
                  .state('home.contact', {
                  url: '/contact/',
                  templateUrl: '/assets/partials/contact.html',
                  controller: 'ContactController',
                  resolve: { loginRequired: loginRequired }
                  }).
                  state("logout", {
                  url: "/logout",
                  templateUrl: null,
                  controller: "LogoutController"
                  });

$urlRouterProvider.otherwise('/login');




 function skipIfLoggedIn($q, $auth) {
      var deferred = $q.defer();
      if ($auth.isAuthenticated()) {
        deferred.reject();
      } else {
        deferred.resolve();
      }
      return deferred.promise;
    }

    function loginRequired($q, $location, $auth) {
      var deferred = $q.defer();
      if ($auth.isAuthenticated()) {
        deferred.resolve();
      } else {
        $location.path('/login');
      }
      return deferred.promise;
    }

}
  ]);

                  emsApp.config(function(uiGmapGoogleMapApiProvider) {
                   uiGmapGoogleMapApiProvider.configure({
                    key: 'AIzaSyD4vzQKlYsXQArSDtFgGJlhkjyjmG8KtJA',
                    v: '3.25',
                    libraries: 'weather,geometry,visualization'
                   });
                  });