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

    generateGrid($('.my-container'), layout);

    $(document.body).on('click', '.edit-element', function () {
        let element = $(this).closest('.my-element');
        let id = element.attr('class');
        if (id.indexOf('my-text') !== -1) {
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
        createVideo(root.data('elementId'), config);
        root.modal('hide');
    });

    $('#modal-photo').on('click', '.btn-primary', function () {
        let root = $(this).closest('.modal');
        let url = $('#photo-url').val();
        if (url.indexOf('res.cloudinary.com/itraphotocloud/image/upload') === -1) {
            $.post('https://api.cloudinary.com/v1_1/cloud9/image/upload', {
                api_key: 891695265656755,
                timestamp: (Date.now() / 1000 | 0),
                upload_preset: 'lrwcwlyh',
                file: url
            }, function (response, status) {
                _createPhoto('http://res.cloudinary.com/itraphotocloud/image/upload/' + response.public_id + '.jpg');
            });
            return;
        }

        function _createPhoto(cloudUrl) {
            let config = {
                width: $('#photo-width').val(),
                height: $('#photo-height').val(),
                url: cloudUrl
            };
            createPhoto(root.data('elementId'), config);
            Dropzone.instances[0].removeAllFiles();
            root.modal('hide');
        }

       _createPhoto(url);
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
        template.append($('<iframe width="' + config.width + '" height="' + config.height + '" ' +
               ' src="' + url + '" ' +
               'frameborder="0" allowfullscreen="true">').attr('data-my-src', config.url)
            .data('mySrc', config.url));
    }

    function createPhoto(id, config) {
        let url = config.url;
        let sizeString = '/c_pad,h_' + config.height + ',w_' + config.width;
        url = insertAfter(url, sizeString, 'upload');
        let template = $('#' + id);
        template.find('.my-icon').remove();
        template.find('img').remove();
        template.append($('<img src="' + url + '"/>').attr('data-my-src', config)
            .data('mySrc', config));
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

    function customSplice(str, substr, position) {
        return [str.slice(0, position), substr, str.slice(position)].join('');
    }

    function insertAfter(str, substr, targetWord) {
        return customSplice(str, substr, str.indexOf(targetWord) + targetWord.length);
    }













});