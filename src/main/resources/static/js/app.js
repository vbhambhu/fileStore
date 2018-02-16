
var floorId = $("#floor-list").val();
//
// $('.user-datails').hover(function() {
//
//     console.log("dsadadada")
//     var e=$(this);
//     e.off('hover');
//
//     e.popover({content: "fdfssf dfsfsfs"}).popover('show');
//
//     // $.get(e.data('poload'),function(d) {
//     //
//     // });
// });


$.get("/api/design/get", { floorid: floorId } ).done(function( response ) {

    var windowWidth = $("#seating-plan").width();
    var windowHeight = $(window).height() - 10;

    var draw = SVG('seating-plan').size(windowWidth,windowHeight);
    draw.svg(response.svgContent);


    //get size of floor
    var floorElem, floor_orig_width, floor_orig_height;
    draw.each(function(i, children) {
        if(this.data('type') == 'orect'){
            floor_orig_width = this.width();
            floor_orig_height = this.height();
            floorElem = this;
        }

    })




    draw.each(function(i, children) {


        //Scale it to fit on window


        var elem = this;
        elem.attr('cursor', 'auto');

        var userId = (typeof elem.data('user-id') === "undefined") ? 0 : elem.data('user-id');

        if(userId !== 0){

            //animate on load
            elem.animate({ duration: 250 }).flip('x');

            var cx = elem.cx()


            $.get("/api/user/get", { userid: userId } ).done(function( userdata ) {



                var tooltip, tooltipHTML;


                var text = draw.text(userdata.firstName + " \n" + userdata.lastName);
                //var text = draw.text(userdata.firstName);
                text.move(elem.x() + 5 ,elem.y()).addClass("usernames");



                elem.fill('#'+userdata.group.color).radius(4)
                elem.addClass('user-datails');



                //add top layer
                var toplayer = draw.rect(60,60).move(elem.x(), elem.y()).attr('fill-opacity',0);


                toplayer.mouseover(function() {
                    this.attr('cursor', 'pointer');

                    //console.log();

                        tooltip = draw.rect(350,300).radius(5).fill({ color: '#f06' });
                        tooltip.addClass('shadow');




                    var ttPos = getTooltipPosition(toplayer, floor_orig_width, floor_orig_height);



                        tooltip.move(ttPos.x, ttPos.y);

                        var fullName = "Name: " + userdata.firstName + " " + userdata.lastName;
                        fullName += "\nemail: " + userdata.email;
                        fullName += "\nStart date: " + userdata.start_date;
                        fullName += "\nEnd date: " + userdata.end_date;
                        fullName += "\nComputer: " + userdata.computer_address;
                        fullName += "\nIs PI?: " + userdata.ispi;
                        fullName += "\nComment: " + userdata.comment;


                        tooltipHTML = draw.text(fullName)
                            .font({ fill: '#fff'})
                            .move(toplayer.x() + toplayer.width() + 10, elem.cy() + 10)
                            .addClass('userDetail');


                })

                toplayer.mouseout(function() {
                        tooltip.remove()
                    tooltipHTML.remove()
                })



            });






            //console.log()

           // text.move(this.x() + 10,this.y()).font({ fill: '#000', family: 'Inconsolata' }).lines(3)

        }

    })



});



function getTooltipPosition(toplayer, floor_orig_width, floor_orig_height) {

    var ttH = 300;
    var ttW = 200;
    var x, y;

    console.log("Box height" +floor_orig_height)
    console.log("Square y" + toplayer.y())

    //try right side
    if (toplayer.x() + toplayer.width() + 10 + ttW < floor_orig_width && toplayer.y() + toplayer.height() + ttH < floor_orig_height){
        x = toplayer.x() + toplayer.width() + 5;
        y = toplayer.cy()
    } else if(toplayer.x() - ttW - 10 > 0 && toplayer.y() - ttH - 10 > 0){ //try left side
        x = toplayer.x() - ttW - 5;
        y = toplayer.cy() - ttH;
    }else if(toplayer.x() - 10 > 0 && toplayer.y() + ttH + 10 < floor_orig_height){ //right top side
        x = toplayer.x() - ttW - 5;
        y = toplayer.cy();
    } else if(toplayer.x() + ttW + 10 < floor_orig_width && toplayer.y() - 10 > 0){ //left top side
        x = toplayer.cx() + (toplayer.width()/2) + 5;
        y = toplayer.cy() - ttH;
    } else {
        x = 10;
        y = 10;
    }

    //     toplayer.cy() < floor_orig_height) {
    //     x = toplayer.x() + toplayer.width() + 5;
    //     y = toplayer.cy();
    // } else if (true) {


    return {'x': x, y: y};
}