var app = angular.module('chatApp', ['ngMaterial']);

app.config(function ($mdThemingProvider) {
    $mdThemingProvider.theme('default')
        .primaryPalette('purple')
        .accentPalette('blue');
});

app.controller('chatController', function ($scope) {
    $scope.messages = [
        {
            'sender': 'USER',
            'text': 'Hello'
		},
        {
            'sender': 'BOT',
            'text': 'Hi, what can i do for you??'
		},
        {
            'sender': 'USER',
            'text': 'How are you??'
		},
        {
            'sender': 'BOT',
            'text': 'I am fine.'
		}
	];
});