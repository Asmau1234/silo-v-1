var emsControllers = angular.module('emsControllers',[]);

emsControllers.controller("IndexController",["$scope","$http", "$auth","$resource","toastr",
                          function($scope,$http, $auth, $resource, toastr){

                    $scope.title= "Hello World";
                     $scope.myDataSource = {
                            chart: {
                                caption: "Emergency Response Application",
                                subCaption: "Frequency Of Reports per Month",
                                numberPrefix: ""
                            },
                            data: [{
                                label: "Conflict Report",
                                value: "880000"
                            }, {
                                label: "Person CA Report",
                                value: "730000"
                            }, {
                                label: "Incident CA Report",
                                value: "590000"
                            }, {
                                label: "Vehicle CA Report",
                                value: "520000"
                            }]
                        };


}]);

emsControllers.controller("LoginController",['$rootScope','$scope','$auth','$location',"$state","$http",'toastr',"$localStorage","$window",
                          function($rootScope,$scope, $auth,$location,$state,$http,toastr,$localStorage, $window){

                    $scope.title= "Hello World";
                    $scope.login = function(){

                       $auth.login($scope.user).then(function(response){
                               var role = response.data.userType;
                                console.log(response);
                                $scope.$storage = $localStorage;
                                $scope.$storage.userProfile = response.data.profile;

                              $scope.isAuthenticated = function(){
                                return $auth.isAuthenticated;

                              };
                               toastr.success('You have successfully signed in');
                               if(role.accountType == "User"){
                                   $location.path("/home");
                                }else if(role.accountType == "Admin") {
                                   $location.path("/location/home");
                                }
                                 else {
                                      $location.path("/home");
                                                           }

                           }).catch(function(response){

                                  toastr.error(response.data);

                         });
                       };

                       $scope.signup=function(){
                       var user= $scope.user;
                       $http.post("/signup", {user:user}).success(function(res){
                                 toastr.success("Signed Up!");
                                 //$location.path("/home");


                              }).error(function(e){
                             toastr.error("Failure. Check Internet Connectivity!");
                                                   });
                                                 };




}]);
emsControllers.controller("MapController",["$scope","$http", "$auth","$resource","toastr","uiGmapGoogleMapApi",
                    function($scope,$http, $auth, $resource, toastr, uiGmapGoogleMapApi){

                    $scope.severitys=[{name: "Emergency"}, {name: "Escalation"}, {name: "Engaging"}];
                    $scope.text="Please insert a location and the possible level of crisis in that location to populate the map below with only said locations data!";
                    $scope.booleans=[{name: "Yes"}, {name: "No"}];
                    $scope.makeConflictreport=false;
                    $scope.viewConflictMap=false;
                    $scope.stateCrisis=false;
                    $scope.lgaCrisis=false;


//display menu item for adding conflict reports
                    $scope.addConflict=function(){
                    $scope.makeConflictreport=true;
                    $scope.viewConflictMap=false;

                    };

//function that saves the form data of conflict report
                    $scope.reportConflict=function(conf){

                 //this retrieves the user's current location lat and lang
                    if(conf.currentLocation === "Yes"){

                    if (navigator.geolocation) {
                         navigator.geolocation.getCurrentPosition(function (position) {

                            var mylat = position.coords.latitude;
                             var mylong = position.coords.longitude;
                              var reportDets= {
                                               "currentLocation": conf.currentLocation,
                                               "levelOfCrisis":conf.levelOfCrisis,
                                               "latitude": mylat,
                                               "longitude": mylong
                                               };
                    $http.post("/ocad/make-conflict-report", {reportDets:reportDets}).success(function(res){
                               toastr.success("Your Report Has Been Submitted!");
                                           $scope.conflictreport = null;

                    }).error(function(e){
                               toastr.error("Failure. Check Internet Connectivity!");
                            });
                          });
                        }
                    }else{

//here i retrieve the lat and lang of selected location via google geolocation api
                     var address= conf.stateOfCrisis + " "+ conf.lgaOfCrisis;
                      // Initialize the Geocoder
                      geocoder = new google.maps.Geocoder();
                      geocoder.geocode({
                                       'address': address
                      }, function (results, status) {
                         if (status == google.maps.GeocoderStatus.OK) {
                              //callback(results[0]);
                              var lat=results[0].geometry.location.lat();
                              var long= results[0].geometry.location.lng();
                         var reportDets={
                                          "currentLocation": conf.currentLocation,
                                          "levelOfCrisis":conf.levelOfCrisis,
                                          "stateOfCrisis":conf.stateOfCrisis,
                                          "lgaOfCrisis": conf.lgaOfCrisis,
                                          "latitude": lat,
                                          "longitude": long
                         };
                         $http.post("/ocad/make-conflict-report", {reportDets:reportDets}).success(function(res){
                                          toastr.success("Your Report Has Been Submitted!");
                                          $scope.conflictreport = null;

                                          }).error(function(e){
                                          toastr.error("Failure. Check Internet Connectivity!");
                                                        });
                                                     }
                              else{
                                  toastr.error("Network Connection Timed Out!");
                                   }
                             });
                                  }


                    };

//function that disabled state and lga buttons
                    $scope.getLocationtable=function(currentlocation){
                          if(currentlocation === "Yes"){
                                  $scope.stateCrisis=true;
                                  $scope.lgaCrisis=true;
                                  //getCurrentLatLang();
                          }else{
                                  $scope.stateCrisis=false;
                                  $scope.lgaCrisis=false;
                                        }
                      };

//display menu for searching of conflicts
                    $scope.viewConflicts=function(){
                    $scope.makeConflictreport=false;
                    $scope.viewConflictMap=true;
                    };

//searches db for reports based on selected parameters.

/*TO-DO: markers now displaying */
                    $scope.search=function(search){
                    var data= {"state": search.state,
                                "lga":search.lga,
                                "severity":search.severity};
                    $http.post("/ocad/search-conflicts", {data:data}).success(function(res){
                                var markers = res;
                                _.forEach(markers, function(marker) {
                                         var m = {
                                                  idKey: marker._id,
                                                  icon: '/assets/images/icon.png',
                                                  coords:{
                                                  latitude: marker.latitude,
                                                  longitude: marker.longitude
                                                 }
                                               };
                                 $scope.map.markers.push(m);
                                           });
                    console.log($scope.map.markers);
                                   // $scope.$apply();

                     }).error(function (e){

                      toastr.error("Oops.Network Error!") ;
                      });

             $scope.map = { center: { latitude: 9, longitude: 8 }, zoom: 6, markers:[] };
                    };


                    $http.get("/states").success(function(response){
                     $scope.states=response;
                     }).error(function (e){

                        toastr.error("Oops.Network Error!");
                    });

                    $scope.getLga = function(state){
                     if(state !== null){
                     $http.get("/lga/"+state).success(function(result){
                        //console.log(result);
                     $scope.lgas = result;
                       });
                        }
                    };




}]);

emsControllers.controller("OCADMainController",["$scope","$http", "$auth","$resource","toastr","$state",
                          function($scope,$http, $auth, $resource, toastr, $state){

                    $scope.title= "Online Child Abuse Database";
                    $scope.booleans=[{name: "Yes"}, {name: "No"}];
                    $scope.gender=[{name: "Female"}, {name: "Male"}];
                    $scope.skincolors=[{name:"Black"}, {name:"Brown"}, {name:"White"}];
                    $scope.races=[{name:"African"},{name:"African American"},{name:"Asian"},{name:"Hispanic"},{name:"Other"}];
                    $scope.hairtypes=[{name:"Afro"},{name:"Braided/Twisted"},{name:"Curly"},{name:"Kinky"},{name:"Straight"}];
                    $scope.religions=[{name:"Christain"},{name:"Jew"},{name:"Muslim"}];
                    $scope.tribes=[{name:"Igbo"},{name:"Hausa"},{name:"Yoruba"},{name:"Other"}];
                    $scope.numberRange=[{name:"0-2"},{name:"3-5"},{name:"6-8"},{name:"9-10"},{name:"More"}];
                    $scope.seatTypes=[{name:"Leather"},{name:"Cotton"},{name:"Not Sure"}];
                    $scope.IncidentTypes=[{name:"Child Trafficking"},{name:"Child Labour"},{name:"Sexual Exploitation Of Child/Children"}];


                    $scope.personReport=false;
                    $scope.vehicleReport=false;
                    $scope.incidentReport=false;
                    $scope.makereportClasses=false;
                    $scope.ocadDash=true;
                    $scope.viewreportClasses=false;
                    $scope.vehiclereportTable=false;
                    $scope.incidentreportTable=false;
                    $scope.personreportTable=false;
                    $scope.person=false;
                                         $scope.vehicle=false;
                                         $scope.incident=false;

                    $http.get("/vehicles").success(function(response){
                    $scope.vehicles=response;
                   // console.log(response);
                    }).error(function (e){

                    toastr.error("Oops.Network Error, could not load list of Vehicles!");
                    });

                    $scope.getMakes = function(vehicle){
                  if(vehicle !== null){
                    $http.get("/make/"+vehicle).success(function(result){
                                            //console.log(result);
                    $scope.vehicleMakes = result;
                    });
                  }
                 };

                 $http.get("/colors").success(function(response){

                 $scope.colors= response;
                 }).error(function (e){

                   toastr.error("Oops.Network Error, could not load list of Colors!");
                 });

                    $scope.personreport={};
                    $scope.submitPersonreport=function(){
                     var reportDets= $scope.personreport;
                     $http.post("/ocad/make-person-report", reportDets).success(function(res){
                                          toastr.success("Your Report Has Been Submitted!");
                                           $scope.personreport = null;

                                          }).error(function(e){
                                          toastr.error("Please re-check your input fields and try again.");
                                          });

                    };

                    $scope.submitVehiclereport=function(){
                    var reportDets= $scope.vehiclereport;
                    $http.post("/ocad/make-vehicle-report", reportDets).success(function(res){
                                         toastr.success("Your Report Has Been Submitted!");
                                         $scope.vehiclereport = null;

                                         }).error(function(e){
                                         toastr.error("Please re-check your input fields and try again.");
                                         });
                    };

                    $scope.submitIncidentreport=function(){
                     var reportDets= $scope.incidentreport;
                     $http.post("/ocad/make-incident-report",reportDets).success(function(res){
                                          toastr.success("Your Report Has Been Submitted!");
                                          $scope.incidentreport = null;

                                          }).error(function(e){
                                          toastr.error("Please re-check your input fields and try again.");
                                          });
                    };

                    $scope.makeReport=function(){
                    $scope.ocadDash=false;
                    $scope.makereportClasses=true;
                    $scope.viewreportClasses=false;
                    $scope.personReport=false;
                    $scope.vehicleReport=false;
                    $scope.incidentReport=false;
                    $scope.ocadDash=false;
                    $scope.vehiclereportTable=false;
                    $scope.incidentreportTable=false;
                    $scope.personreportTable=false;
                    $scope.person=false;
                    $scope.vehicle=false;
                    $scope.incident=false;
                    };

                    $scope.viewReports=function(){
                    $scope.viewreportClasses=true;
                    $scope.ocadDash=false;
                    $scope.makereportClasses=false;
                    $scope.personReport=false;
                    $scope.vehicleReport=false;
                    $scope.incidentReport=false;
                    $scope.ocadDash=false;
                    $scope.vehiclereportTable=false;
                    $scope.incidentreportTable=false;
                    $scope.personreportTable=false;
                    $scope.person=false;
                    $scope.vehicle=false;
                    $scope.incident=false;
                    };

                    $scope.showPersonreport=function(){
                    $scope.viewreportClasses=false;
                    $scope.ocadDash=false;
                    $scope.makereportClasses=true;
                    $scope.personReport=true;
                    $scope.vehicleReport=false;
                    $scope.incidentReport=false;
                    $scope.ocadDash=false;
                    $scope.vehiclereportTable=false;
                    $scope.incidentreportTable=false;
                    $scope.personreportTable=false;

                    };

                    $scope.showVehiclereport=function(){
                    $scope.viewreportClasses=false;
                    $scope.ocadDash=false;
                    $scope.makereportClasses=true;
                    $scope.personReport=false;
                    $scope.vehicleReport=true;
                    $scope.incidentReport=false;
                    $scope.ocadDash=false;
                    $scope.vehiclereportTable=false;
                    $scope.incidentreportTable=false;
                    $scope.personreportTable=false;
                    $scope.person=false;
                    $scope.vehicle=false;
                    $scope.incident=false;
                    };

                    $scope.showIncidentreport=function(){
                    $scope.viewreportClasses=false;
                    $scope.ocadDash=false;
                    $scope.makereportClasses=true;
                    $scope.personReport=false;
                    $scope.vehicleReport=false;
                    $scope.incidentReport=true;
                    $scope.ocadDash=false;
                    $scope.vehiclereportTable=false;
                    $scope.incidentreportTable=false;
                    $scope.personreportTable=false;
                    $scope.person=false;
                    $scope.vehicle=false;
                    $scope.incident=false;
                    };

                    $scope.viewPersonreports=function(){
                     $scope.personReport=false;
                     $scope.vehicleReport=false;
                     $scope.incidentReport=false;
                     $scope.makereportClasses=false;
                     $scope.ocadDash=false;
                     $scope.viewreportClasses=true;
                     $scope.vehiclereportTable=false;
                     $scope.incidentreportTable=false;
                     $scope.personreportTable=true;
                     $scope.person=false;
                     $scope.vehicle=false;
                     $scope.incident=false;
                     $http.get("/show-person-report").success(function(response){
                               $scope.personreports=response;
                               //console.log(response);
                            }).error(function (e){

                               toastr.error("Oops.Network Error!");
                            });
                    };

                    $scope.viewVehiclereports=function(){
                     $scope.personReport=false;
                     $scope.vehicleReport=false;
                     $scope.incidentReport=false;
                     $scope.makereportClasses=false;
                     $scope.ocadDash=false;
                     $scope.viewreportClasses=true;
                     $scope.vehiclereportTable=true;
                     $scope.incidentreportTable=false;
                     $scope.personreportTable=false;
                     $scope.person=false;
                     $scope.vehicle=false;
                     $scope.incident=false;
                     $http.get("/show-vehicle-report").success(function(response){
                              $scope.vehiclereports=response;
                                                    //console.log(response);
                              }).error(function (e){

                              toastr.error("Oops.Network Error!");
                              });
                    };

                    $scope.viewIncidentreports=function(){
                     $scope.personReport=false;
                     $scope.vehicleReport=false;
                     $scope.incidentReport=false;
                     $scope.makereportClasses=false;
                     $scope.ocadDash=false;
                     $scope.viewreportClasses=true;
                     $scope.vehiclereportTable=false;
                     $scope.incidentreportTable=true;
                     $scope.personreportTable=false;
                     $http.get("/show-incident-report").success(function(response){
                               $scope.incidentreports=response;
                               //console.log(response);
                               }).error(function (e){

                               toastr.error("Oops.Network Error!");
                               });
                    };

                     $scope.fullPReport={};

                    $scope.showPReport= function(report){
                    $scope.person=true;
                    $scope.vehicle=false;
                    $scope.incident=false;
                    $scope.personReport=false;
                    $scope.vehicleReport=false;
                    $scope.incidentReport=false;
                    $scope.makereportClasses=false;
                    $scope.ocadDash=false;
                    $scope.viewreportClasses=false;
                    $scope.vehiclereportTable=false;
                    $scope.incidentreportTable=false;
                    $scope.personreportTable=false;
                   // $state.go('home.pdetails',{id:report._id});
                     $http.get("/view-preport-details/" + report._id).success(function(res){


                                         $scope.fullPReport=res;
                                         //console.log(res);

                                         });
                    };

                    $scope.showVReport= function(report){
                    //$state.go('home.vdetails',{id:report._id});
                     $scope.person=false;
                     $scope.vehicle=true;
                     $scope.incident=false;
                     $scope.personReport=false;
                     $scope.vehicleReport=false;
                     $scope.incidentReport=false;
                     $scope.makereportClasses=false;
                     $scope.ocadDash=false;
                     $scope.viewreportClasses=false;
                     $scope.vehiclereportTable=false;
                     $scope.incidentreportTable=false;
                     $scope.personreportTable=false;
                    $http.get("/view-vreport-details/"+report._id).success(function(response){


                    $scope.fullVReport= response;
                    //console.log(response);

                    });
                    };

                    $scope.showIReport= function(report){
                    //$state.go('home.idetails',{id:report._id});
                    $scope.person=false;
                    $scope.vehicle=false;
                    $scope.incident=true;
                    $scope.personReport=false;
                    $scope.vehicleReport=false;
                    $scope.incidentReport=false;
                    $scope.makereportClasses=false;
                    $scope.ocadDash=false;
                    $scope.viewreportClasses=false;
                    $scope.vehiclereportTable=false;
                    $scope.incidentreportTable=false;
                    $scope.personreportTable=false;
                    $http.get("/view-ireport-details/"+report._id).success(function(response){

                    $scope.fullIReport= response;
                    console.log(response);

                                        });

                    };

                    $scope.printPage= function(){
                    $scope.in= false;
                    window.print();

                     };





}]);

emsControllers.controller("ReportDetailsController",["$scope","$http", "$auth","$resource","toastr",
                          function($scope,$http, $auth, $resource, toastr){

                    $scope.title= "Hello World";
                    //$scope.Report=Report;



}]);

emsControllers.controller("CorrelationController",["$scope","$http", "$auth","$resource","toastr",
                          function($scope,$http, $auth, $resource, toastr){

                    $scope.title= "Hello World";
                    //$scope.Report=Report;
                    $scope.makeCorrelation=function(){

                    $http.get("/correlation-matrix").success(function(response){

                    $scope.person= response.person;
                    $scope.vehicle=response.vehicle;
                    $scope.incident=response.incident;

                    var p= $scope.person;
                    var v= $scope.vehicle;
                    var inc= $scope.incident;
                    $scope.newArray=[{}];

                    angular.forEach($scope.person, function (value, key) {
                            var nameOfSuspect = value.suspectProfile.nameOfSuspect;
                            var genderOfSuspect = value.suspectProfile.genderOfSuspect;
                            var addressOfSuspect = value.suspectProfile.contactAddress;
                            var locationOfSuspect = value.lastplaceSeen;
                            var psuspicionText = value.suspicionText;
                    angular.forEach($scope.vehicle, function (value,key) {
                            var nameOfOwner = value.nameofOwner;
                            var genderOfOwner = value.genderofOwner;
                            var addressOfOwner = value.addressofOwner;
                            var locationOfOwner = value.lastlocationSeen;
                            var vsuspicionText = value.suspicionText;
                    angular.forEach($scope.incident, function (value,key) {
                            var nameOfInc = value.nameOfSuspect;
                            var genderOfInc = value.genderOfSuspect;
                            var addressOfInc = value.addressofIncident;
                            var locationOfInc = value.locationofIncident;
                            var isuspicionText = value.suspicionText;
                            if(nameOfSuspect === nameOfOwner && nameOfSuspect === nameOfInc){
                                 if(genderOfSuspect === genderOfOwner && genderOfSuspect === genderOfInc){
                                   if(addressOfSuspect === addressOfOwner && addressOfSuspect === addressOfInc){
                                     if(locationOfSuspect === locationOfOwner && locationOfSuspect === locationOfInc){
                                       if(psuspicionText === vsuspicionText && psuspicionText === isuspicionText){
                                          $scope.newArray.push({"name": nameOfSuspect,
                                                                "gender": genderOfSuspect,
                                                                "address": addressOfSuspect,
                                                                "location": locationOfSuspect,
                                                                "suspicious": psuspicionText});
                                                               }
                                                              }
                                                             }
                            }
                            }
                            });
                        });
                     });

                     });
                    };



}]);

emsControllers.controller("AboutController",["$scope","$http", "$auth","$resource","toastr",
                          function($scope,$http, $auth, $resource, toastr){

                    $scope.title= "Hello World";


}]);

emsControllers.controller("ContactController",["$scope","$http", "$auth","$resource","toastr",
                          function($scope,$http, $auth, $resource, toastr){

                    $scope.title= "Hello World";


}]);

emsControllers.controller('LogoutController', ["$location","$auth","toastr",'$localStorage',
                                            function($location, $auth, toastr, $localStorage) {


                        if (!$auth.isAuthenticated()) {
                        return;
                        }
                        $auth.logout()
                          .then(function() {

                          console.log($localStorage);
                          $localStorage.$reset();
                          $location.path('/login');
                          toastr.info('You have been logged out');

                          });
                      }]);