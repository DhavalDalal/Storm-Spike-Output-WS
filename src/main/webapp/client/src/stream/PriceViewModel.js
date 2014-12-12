PriceViewModel = function(id, rate) {
	this.id = ko.observable(id);
	this.rate = ko.observable(Number(rate).toFixed(2));
	this.changeDirectionClass = ko.observable();
};
