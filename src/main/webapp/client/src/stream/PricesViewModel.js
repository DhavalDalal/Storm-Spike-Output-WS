PricesViewModel = function() {
	this.prices = ko.observableArray([]);
};

PricesViewModel.prototype._indexOf = function(price) {
	var allPrices = this.prices();
	for(var i = 0, j = allPrices.length; i < j; i++){
	  	var priceViewModel = allPrices[i];
	  	if(priceViewModel.id() === price.id) {
	  		return priceViewModel;
	  	}
	};
	return false;
};

PricesViewModel.prototype._sort = function() {
	this.prices.sort(function(left, right) {
		return left.id() == right.id() ? 0 : (left.id() < right.id() ? -1 : 1)
	});
};

PricesViewModel.prototype.addOrUpdatePrice = function(price) {
	var priceViewModel = this._indexOf(price);
	if(priceViewModel === false) {
		priceViewModel = new PriceViewModel(price.id, Number(price.rate).toFixed(2));
		this.prices.push(priceViewModel);
		this._sort();
	} else {
		var oldRate = priceViewModel.rate();
		priceViewModel.id(price.id);
		var currentRate = Number(price.rate).toFixed(2);
		priceViewModel.rate(currentRate);
		this._updateChangeDirection(priceViewModel, oldRate, currentRate);
	}
};

PricesViewModel.prototype._updateChangeDirection = function(priceViewModel, oldRate, currentRate) {
	if(oldRate > currentRate) {
		priceViewModel.changeDirectionClass('glyphicon glyphicon-arrow-down green');
		return;
	}
	 
	if(oldRate < currentRate) {
		priceViewModel.changeDirectionClass('glyphicon glyphicon-arrow-up red');
		return;
	} 
	
	if(oldRate === currentRate) {
		priceViewModel.changeDirectionClass('');
		return;
	} 
};

