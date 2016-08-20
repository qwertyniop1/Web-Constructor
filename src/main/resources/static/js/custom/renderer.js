var myRenderer = {
    // Layout management
    renderLayout: function (container, layoutId) {
        this.renderGrid(container, this.layouts[layoutId]);
    },
    renderGrid: function (container, rows) {
        rows.forEach(function (row) {
            let rowTemplate = $('<div class="row"></div>');
            row.forEach(function (col) {
                rowTemplate.append('<div class="my-content col-md-' + col + '"></div>');
            });
            container.append(rowTemplate);
        }) ;
    },
    layouts: [
        [[12], [12]],
        [[12], [6, 6]],
        [[6, 6], [12]],
        [[6, 6], [6, 6]]
    ],

    // Elements types
    idCounter: 1,
    createElement: function(type, editable, icon) {
        let wrapper = editable
            ? $(baseElement).append($('<img class="my-icon" height="256" width="256" src="' + icon + '">'))
            : $('<div class="my-element"></div>');
        wrapper.addClass(type);
        wrapper.attr('id', 'element-' + this.idCounter++);
        return wrapper;
    },

    // Elements rendering
    load: function (element) {
        this.loadElement(element, element.type.toLowerCase());
    },
    loadElement: function(element, type, icon) {
        let base = $('.my-content:eq(' + element.location + ')');
        let created = myRenderer.createElement('my-' + type, icon !== undefined, icon);
        base.append(created);
        if ((element.url.length === 0 && type !== 'text' && type !== 'table') || (element.text.length === 0 && (type === 'text' || type === 'table'))) return;
        this.createMethods[type](created.attr('id'), {
            width: element.width,
            height: element.height,
            url: element.url,
            text: element.text,
            autoplay: element.autoplay,
            loop: element.videoLoop,
            chart: element.chart
        });
    },
    createMethods: {
        'text': function (id, config) {
            let template = $('#' + id);
            template.find('.my-icon').remove();
            template.find('.my-text-content').remove();
            template.append('<div class="my-text-content">' + config.text + '</div>');
        },
        'image': function (id, config) {
            let url = config.url;
            let sizeString = '/c_pad,h_' + config.height + ',w_' + config.width;
            url = myRenderer.insertAfter(url, sizeString, 'upload');
            let template = $('#' + id);
            template.find('.my-icon').remove();
            template.find('img').remove();
            template.append($('<img src="' + url + '"/>').attr('data-my-src', config)
                .data('mySrc', config));
        },
        'video': function (id, config) {
            let url = myRenderer.parseYoutubeUrl(config.url);
            if (url.length === 1) {
                customAlert(INVALID_URL);
                return;
            }
            url = 'https://www.youtube.com/embed/' + url + '?';
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
                'frameborder="0" allowfullscreen="true">')
                .attr('data-my-src', config.url).data('mySrc', config.url)
                .attr('data-auto', config.autoplay).data('auto', config.autoplay)
                .attr('data-loop', config.loop).data('loop', config.loop));
        },
        'table': function (id, config) {
            let template = $('#' + id);
            let table = $('<table class="table table-striped">' + config.text + '</table>').attr('data-chart', config.chart).data('chart', config.chart);
            let canvas = $('<div class="my-chart"></div>');
            template.find('.my-icon').remove();
            template.find('table').remove();
            template.find('.my-chart').remove();
            template.append(table);
            if (config.chart) {
                template.append(canvas);
                myRenderer.charts.createChart(table, canvas);
            }
        }
    },

    parseYoutubeUrl: function(url) {
        function check(str) {
            let pos = url.indexOf(str);
            return pos !== -1 ? pos + str.length : 0;
        }
        let position = check('youtube.com/watch?v=') ||
            check('youtu.be/');
        return position !== 0 ? url.slice(position) : ' ';
    },
    customSplice: function(str, substr, position) {
        return [str.slice(0, position), substr, str.slice(position)].join('');
    },
    insertAfter: function(str, substr, targetWord) {
        return this.customSplice(str, substr, str.indexOf(targetWord) + targetWord.length);
    },
    setLocaleMessage: function (message) {
        INVALID_URL = message;
    },
    charts: {
        createChart: function(table, context) {
            this._createChart(this.createCanvas(context), this.getLabels(table), this.getData(table), this.generateColors(table));
        },
        createCanvas: function (container) {
            container.html('');
            let canvas = $('<canvas width="400" height="400"></canvas>');
            container.append(canvas);
            return canvas;
        },
        _createChart: function (context, labels, dataset, colors) {
            let data = {
                labels: labels,
                datasets: [
                    {
                        data: dataset,
                        backgroundColor: colors,
                        hoverBackgroundColor: colors
                    }]
            };

            let myPieChart = new Chart(context, {
                type: 'pie',
                data: data,
                // options: options
            });
        },
        getLabels: function (table) {
            let result = [];
            table.find('td:first-child').each(function (index, label) {
                result.push($(label).text());
            });
            return result;
        },
        getData: function (table) {
            let result = [];
            table.find('td:last-child').each(function (index, label) {
                result.push(parseFloat($(label).text()));
            });
            return result;
        },
        generateColors: function (table) {
            let result = [];
            table.find('td:first-child').each(function (index, label) {
                result.push(myRenderer.charts.getRandomColor());
            });
            return result;
        },
        getRandomColor: function () {
            let letters = '0123456789ABCDEF';
            let color = '#';
            for (let i = 0; i < 6; i++ ) {
                color += letters[Math.floor(Math.random() * 16)];
            }
            return color;
        }
    }
};

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

var INVALID_URL = 'Invalid URL';