var emsApp = angular.module('emsApp', [
    'ngRoute',
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
                 controller: 'LoginController'
                  })
                  .state('login', {
                  url: '/login',
                  templateUrl: '/assets/partials/login.html',
                  controller: 'LoginController'
                  })
                  .state('home', {
                  url: '/home',
                  templateUrl: '/assets/partials/home.html',
                  controller: 'IndexController'
                  })
                  .state('home.map', {
                  url: '/map',
                  templateUrl: '/assets/partials/map.html',
                  controller: 'MapController'
                  })
                  .state('home.ocad', {
                  url: '/ocad/home',
                  templateUrl: '/assets/partials/ocad/main.html',
                  controller: 'OCADMainController'
                  })
                  .state('home.about', {
                  url: '/about',
                  templateUrl: '/assets/partials/about.html',
                  controller: 'AboutController'
                  });
                 /* .state('home.pdetails', {
                  url: '/view-preport-details/:id',
                  templateUrl: '/assets/partials/ocad/reportdetails.html',
                  controller: 'OCADMainController'
                  })
                  .state('home.idetails', {
                  url: '/view-ireport-details/:id',
                  templateUrl: '/assets/partials/ocad/reportdetails.html',
                  controller: 'OCADMainController'
                  })
                  .state('home.vdetails', {
                  url: '/view-vreport-details/:id',
                  templateUrl: '/assets/partials/ocad/reportdetails.html',
                  controller: 'OCADMainController'
                  });*/

$urlRouterProvider.otherwise('/');





                  }
                  ]);

                  emsApp.config(function(uiGmapGoogleMapApiProvider) {
                   uiGmapGoogleMapApiProvider.configure({
                    key: 'AIzaSyD4vzQKlYsXQArSDtFgGJlhkjyjmG8KtJA',
                    v: '3.25',
                    libraries: 'weather,geometry,visualization'
                   });
                  });