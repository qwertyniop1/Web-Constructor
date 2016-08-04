$(document).ready(function () {

    // config
    var layout =  [[12], [6, 6]];
    var VIDEO_HEIGHT = 315;
    var VIDEO_WIDTH = 560;
    var MAX_ELEMENT_IN_BLOCK = 3;

    var baseElement = '\
            <div class="my-element">\
                <div class="toolbar">\
                    <a href="#" class="btn btn-info btn-xs edit-element" rel="tooltip" title="Settings">\
                        <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>\
                    </a>\
                    <a href="#" class="btn btn-danger btn-xs del-element" rel="tooltip" title="Remove">\
                        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>\
                    </a>\
                </div>\
            </div>';

    // setup toolbar and edit field
    var toolbar = $('.my-toolbar');

    $('.my-tool', toolbar).draggable({
        revert: 'invaid',
        helper: 'clone',
        containment: 'body',
        cancel: false
    });

    generateGrid($('.my-container'), layout);

    $(document.body).on('click', '.edit-element', function () {
        let element = $(this).closest('.my-element');
        let id = element.attr('class');
        if (id.indexOf('my-text') !== -1) {
        } else if (id.indexOf('my-image') !== -1) {
            $('#modal-photo').modal('show');
        } else if (id.indexOf('my-video') !== -1) {
            $('#modal-video').attr('data-element-id', element.attr('id'));
            $('#modal-video').data('elementId', element.attr('id'));
            let frame = element.find('iframe');
            if (frame.length) {
                $('#video-width').val(frame.attr('width'));
                $('#video-height').val(frame.attr('height'));
                $('#video-url').val(frame.attr('src'));
                $('#video-auto').prop('checked', false); //TODO fix
                $('#video-loop').prop('checked', false);
            } else {
                $('#video-width').val(VIDEO_WIDTH);
                $('#video-height').val(VIDEO_HEIGHT);
                $('#video-url').val('');
                $('#video-auto').prop('checked', false);
                $('#video-loop').prop('checked', false);
            }
            $('#modal-video').modal('show');
        } else if (id.indexOf('my-comments') !== -1) {
        } else if (id.indexOf('my-ratings') !== -1) {
        }

    });

    $(document.body).on('click', '.del-element', function () {
        $(this).closest('.my-element').remove();
    });

    $('#modal-video').on('click', '.btn-primary', function () {
        let root = $(this).closest('.modal');
        let config = {
            width: $('#video-width').val(),
            height: $('#video-height').val(),
            url: $('#video-url').val(),
            loop: $('#video-loop').prop('checked'),
            autoplay: $('#video-auto').prop('checked')
        };
        createVideo(root.data('elementId'), config);
    });

    function generateGrid(container, rows) {

        rows.forEach(function (row) {
            let rowTemplate = $('<div class="row"></div>');
            row.forEach(function (col) {
                rowTemplate.append('<div class="my-content col-md-' + col + '"></div>');
            });
            container.append(rowTemplate);
        }) ;

        var editField = $('.my-content');

        editField.droppable({
            accept: '.my-toolbar > .my-tool',
            drop: function (event, ui) {
                addElement(ui.draggable, $(this));
            }
        });

        let shouldDelete = false;
        editField.sortable({
            cursor: 'move',
            connectWith: editField,
            containment: 'body',
            tolerance: 'pointer',
            over: function () {
                shouldDelete = false;
            },
            out: function () {
                shouldDelete = true;
            },
            beforeStop: function (event, ui) {
                if (shouldDelete == true) {
                    ui.item.remove();
                }
            }
        });
    }

    function addElement(item, container) {
        if (container.find('.my-element').length >= MAX_ELEMENT_IN_BLOCK) {
            customAlert('You can\'t put more than ' + MAX_ELEMENT_IN_BLOCK + ' elements in block'); //TODO locale
            return;
        }
        let id = item.attr('id');
        if (id === 'tool-text') {
            container.append(createElement('my-text', 'images/text.png'))
        } else if (id === 'tool-image') {
            container.append(createElement('my-image', 'images/camera.png'))
        } else if (id === 'tool-video') {
            container.append(createElement('my-video', 'images/movie.png'))
        } else if (id === 'tool-comment') { // TODO add
            container.append(createElement('my-comments', ''))
        } else if (id === 'tool-rating') {
            container.append(createElement('my-rating', ''))
        }
    }

    var idCounter = 1;
    function createElement(type, icon) {
        let wrapper = $(baseElement);
        wrapper.addClass(type);
        wrapper.attr('id', 'element-' + idCounter++);
        wrapper.append($('<img class="my-icon" height="256" width="256" src="' + icon + '">'));
        return wrapper;
    }



    function createVideo(id, config) {
        let url = config.url + '?';
        if (config.autoplay) {
            url += 'autoplay=1&'
        }
        if (config.loop) {
            url += 'loop=1'
        }
        let template = $('#' + id);
        template.find('.my-icon').remove();
        template.find('iframe').remove();
        template.append('<iframe width="' + config.width + '" height="' + config.height + '" ' +
               ' src="' + url + '" ' +
               'frameborder="0" allowfullscreen="true">');
    }

    function customAlert(message, title = '') {
        let alert = $('.my-notification');
        alert.find('strong').text(title);
        alert.find('p').text(message);
        alert.alert();
        alert.fadeTo(2000, 500).slideUp(500, function(){
            alert.slideUp(500);
        });
    }














});