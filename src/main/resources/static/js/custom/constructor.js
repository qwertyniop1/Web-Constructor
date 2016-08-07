// TODO validation input
 $(document).ready(function () {

    // setup toolbar and edit field
    var toolbar = $('.my-toolbar');

    $('.my-tool', toolbar).draggable({
        revert: 'invaid',
        helper: 'clone',
        containment: 'body',
        cancel: false
    });

    Dropzone.options.uploadArea = {
        url: 'https://api.cloudinary.com/v1_1/cloud9/image/upload',
        acceptedFiles:'.jpg,.png,.jpeg,.gif',
        uploadMultiple: false,
        maxFiles: 1,
        addRemoveLinks: true,
        //dictDefaultMessage: '', //TODO dict
        sending: function (file, xhr, formData) {
            formData.append('api_key', 891695265656755);
            formData.append('timestamp', Date.now() / 1000 | 0);
            formData.append('upload_preset', 'lrwcwlyh ');
        },
       /* complete: function (file) {
            //this.removeAllFiles();
        },*/
        success: function (file, response) {
            $('#photo-url').val('http://res.cloudinary.com/itraphotocloud/image/upload/' + response.public_id + '.jpg');
        }
    };

    tinymce.init({
        selector: '#text-area'
    });

    $(document.body).on('click', '.edit-element', function () {
        let element = $(this).closest('.my-element');
        let id = element.attr('class');
        if (id.indexOf('my-text') !== -1) {
            $('#modal-text').attr('data-element-id', element.attr('id'))
                .data('elementId', element.attr('id'));
            let text = element.find('.my-text-content');
            if (text.length) {
                tinyMCE.get('text-area').setContent(text.html());
            } else {
                tinyMCE.get('text-area').setContent('Put your text here...');
            }
            $('#modal-text').modal('show');
        } else if (id.indexOf('my-image') !== -1) {
            $('#modal-photo').attr('data-element-id', element.attr('id'))
                .data('elementId', element.attr('id'));
            let img = element.find('img:not(.my-icon)');
            if (img.length) {
                $('#photo-width').val(img.data('mySrc').width);
                $('#photo-height').val(img.data('mySrc').height);
                $('#photo-url').val(img.data('mySrc').url);
            } else {
                $('#photo-width').val(VIDEO_WIDTH);
                $('#photo-height').val(VIDEO_HEIGHT);
                $('#photo-url').val('');
            }
            $('#photo-url').closest('.form-group').removeClass('has-error')
            $('.help-block').addClass('hidden');
            $('#modal-photo').modal('show'); //TODO lol
        } else if (id.indexOf('my-video') !== -1) {
            $('#modal-video').attr('data-element-id', element.attr('id'));
            $('#modal-video').data('elementId', element.attr('id'));
            let frame = element.find('iframe');
            if (frame.length) {
                $('#video-width').val(frame.attr('width'));
                $('#video-height').val(frame.attr('height'));
                $('#video-url').val(frame.data('mySrc'));
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
        myRenderer.createMethods['video'](root.data('elementId'), config);
        root.modal('hide');
    });

    $('#modal-text').on('click', '.btn-primary', function () {
        let root = $(this).closest('.modal');
        let content = tinyMCE.get('text-area').getContent();
        myRenderer.createMethods['text'](root.data('elementId'), {text: content});
        root.modal('hide');
    });

    $('#modal-photo').on('click', '.btn-primary', function () {
        let root = $(this).closest('.modal');
        let url = $('#photo-url').val();
        if (url.indexOf('res.cloudinary.com/itraphotocloud/image/upload') === -1) {
            $('.glyphicon-refresh-animate').show();
            $.post('https://api.cloudinary.com/v1_1/cloud9/image/upload', {
                api_key: 891695265656755,
                timestamp: (Date.now() / 1000 | 0),
                upload_preset: 'lrwcwlyh',
                file: url
            })
                .done(function (response) {
                    _createPhoto('http://res.cloudinary.com/itraphotocloud/image/upload/' + response.public_id + '.jpg');
                })
                .fail(function (xhr, status, error) {
                    $('#photo-url').closest('.form-group').addClass('has-error')
                    $('.help-block').removeClass('hidden');
                })
                .always(function () {
                    $('.glyphicon-refresh-animate').hide();
                });
            return;
        }

        function _createPhoto(cloudUrl) {
            let config = {
                width: $('#photo-width').val(),
                height: $('#photo-height').val(),
                url: cloudUrl
            };
            myRenderer.createMethods['image'](root.data('elementId'), config);
            Dropzone.instances[0].removeAllFiles();
            root.modal('hide');
        }

       _createPhoto(url);
    });

    $('.my-layout-set').on('click', function () {
        currentLayout = getLayoutFromId($(this).attr('id'));
        recreateLayout($('.my-container'), currentLayout);
    });



});

// config
var VIDEO_HEIGHT = 315;
var VIDEO_WIDTH = 560;
var MAX_ELEMENT_IN_BLOCK = 3;
var layouts = [ // TODO delete
    [[12], [12]],
    [[12], [6, 6]],
    [[6, 6], [12]],
    [[6, 6], [6, 6]]
];

var currentLayout = 0;

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

function recreateLayout(container, layoutId) {
    container.html('');
    generateGrid(container, layouts[layoutId]);
}

function generateGrid(container, rows) {

    myRenderer.renderGrid(container, rows);

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

var map = {};
map['text'] = '/images/text.png';
map['image'] = '/images/camera.png';
map['video'] = '/images/movie.png';
map['table'] = '/images/table.png';
map['comment'] = '';
map['rating'] = '';

var processElement = {};
processElement['image'] = function (element) {
    let data = element.find('img:not(.my-icon)').data('mySrc');
    return {
        width: data !== null ? data.width : 0,
        height: data !== null ? data.height : 0,
        url: data !== null ? data.url : '',
        text: ''
    }
};
processElement['text'] = function (element) {
    let data = element.find('.my-text-content');
    console.log(data);
    return {
        width: 0,
        height: 0,
        url: '',
        text: data.length !== 0 ? data.html() : ''
    }
};
processElement['video'] = function (element) {
    let video = element.find('iframe');
    return {
        width: video.length !== 0 ? video.attr('width') : 0,
        height: video.length !== 0 ? video.attr('height') : 0,
        url: video.length !== 0 ? video.data('mySrc') : '',
        text: ''
    }
};

function loadForEdit(element) {
    let type = element.type.$name.toLowerCase();
    let _element = map['tool-' + type];
    myRenderer.loadElement(element, type, map[type]);
}

function saveElement(element) {
    let regex = /my-element my-([\w]+)/;
    let type = element.attr('class').match(regex)[1];
    let data = processElement[type](element);
    return {
        type: type,
        location:  $('.my-content').index(element.closest('.my-content')),
        width: data.width,
        height: data.height,
        url: data.url,
        text: data.text
    };
}

function addElement(item, container) {
    if (container.find('.my-element').length >= MAX_ELEMENT_IN_BLOCK) {
        customAlert('You can\'t put more than ' + MAX_ELEMENT_IN_BLOCK + ' elements in block'); //TODO locale
        return;
    }
    let type = item.attr('id').slice(5); //FIXME loh
    let icon = map[type];
    container.append(myRenderer.createElement('my-' + type, true, icon));
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

function getLayoutFromId(id) {
    let regex = /layout-([0-9]+)/;
    return id.match(regex)[1] - 1;
}

$.postJSON = function(url, data, callback, token) {
    return jQuery.ajax({
        'type': 'POST',
        'url': url,
        'contentType': 'application/json',
        'data': JSON.stringify(data),
        'dataType': 'json',
        'headers': { 'X-CSRF-TOKEN': token },
        // 'success': callback
        'complete': callback
    });
};