var ContentHeight = 150;
var TimeToSlide = 250.0;

function hideAccordionContainer(id) {
	document.getElementById( 'AccordionContainer' + id ).style.display = 'none';
}

function hasClass(ele,cls) {
	return ele.className.match(new RegExp('(\\s|^)'+cls+'(\\s|$)'));
}
 
function addClass(ele,cls) {
	if (!this.hasClass(ele,cls)) {
		ele.className = ele.className.replace(/^\s+|\s+$/g,'')
		ele.className += " "+cls;
	}
}
 
function removeClass(ele,cls) {
	if (hasClass(ele,cls)) {
		var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');
		ele.className=ele.className.replace(reg,' ');
	}
}

function isOpened( index ) {
	var clickedId = "Accordion" + index + "Content";
	var titleId = "Accordion" + index + "Title";
	var titleblock = document.getElementById(titleId);
	
	if (hasClass(titleblock, "acc-opened")) {
		return true;
	} else {
		return false;
	}
}

function runAccordion(index)
{
	var clickedId = "Accordion" + index + "Content";
	var titleId = "Accordion" + index + "Title";
	var current = document.getElementById(clickedId);
	//if ( current.isOpen == true ) {
	if ( isOpened(index) == true ) {
		closeAccordionContainer( clickedId );
		switchoff( titleId );
	} else {
		openAccordionContainer( clickedId );
		switchon( titleId );
	}
}

function switchoff (id)
{
	var titleblock = document.getElementById(id);
	if (hasClass(titleblock, "acc-opened")) {
		removeClass(titleblock, "acc-opened");
	}
	addClass(titleblock, "acc-closed");
}

function switchon (id)
{
	var titleblock = document.getElementById(id);
	if (hasClass(titleblock, "acc-closed")) {
		removeClass(titleblock, "acc-closed");
	}
	addClass(titleblock, "acc-opened");
}

function openAccordionContainer(id) {
	setTimeout("animate(" + new Date().getTime() + "," + TimeToSlide + ",'"
	  + "" + "','" + id + "')", 33);
	var opened = document.getElementById(id);
	opened.isOpen = true;	// Set isOpen to true
}

function closeAccordionContainer(id) {
	setTimeout("animate(" + new Date().getTime() + "," + TimeToSlide + ",'"
	  + id + "','" + "" + "')", 33);
	var closed = document.getElementById(id);
	closed.isOpen = false;	// Set isOpen to false
}

function animate(lastTick, timeLeft, closingId, openingId)
{  
	var curTick = new Date().getTime();
	var elapsedTicks = curTick - lastTick;

	var opening = (openingId == '') ? null : document.getElementById(openingId);
	var closing = (closingId == '') ? null : document.getElementById(closingId);
	
	if(timeLeft <= elapsedTicks)
	{
		if(opening != null) {
			opening.style.height = ContentHeight + 'px';		//opening.offsetHeight??
		}
		if(closing != null)
		{
			closing.style.display = 'none';
			closing.style.height = '0px';
		}
		return;
	}

	timeLeft -= elapsedTicks;
	var newClosedHeight = Math.round((timeLeft/TimeToSlide) * ContentHeight);

	if(opening != null)
	{
		if(opening.style.display != 'block')
		opening.style.display = 'block';
		opening.style.height = (ContentHeight - newClosedHeight) + 'px';
	}

	if(closing != null) {
		closing.style.height = newClosedHeight + 'px';
	}

	setTimeout("animate(" + curTick + "," + timeLeft + ",'"
	  + closingId + "','" + openingId + "')", 33);
}
