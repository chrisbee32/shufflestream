module.exports = function (grunt) {
    var mozjpeg = require('imagemin-mozjpeg');
    require('time-grunt')(grunt);

    // Load Grunt modules
    require('load-grunt-tasks')(grunt);

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        sass: {
            dist: {
                options: {
                    style: 'expanded'
                },
                files: {
                    'WebContent/statics/css/loupe.css': 'WebContent/statics/scss/loupe.scss',
                }
            }
        },

        autoprefixer: {
            options: {
                map: false
            },
            dist: {
                files: {
                    'WebContent/statics/css/loupe.css': 'WebContent/statics/css/loupe.css',
                }
            }
        },

        cssmin: {
            target: {
                files: {
                    'WebContent/statics/css/loupe.min.css': 'WebContent/statics/css/loupe.css'
                }
            }
        },

        concat: {
            options: {
                separator: ';',
                stripBanners: true
            },
            dist: {
                src: ['js/vendor/jquery-1.11.1.min.js', 'js/vendor/picturefill.js', 'components/slick/slick.js', 'components/header/header.js', 'js/plugins.js', 'js/hp.js'],
                dest: 'js/master.js'
            }
        },


        uglify: {
            options: {
                mangle: false
            },
            dist: {
                files: {
                    'js/master.min.js': ['js/master.js']
                }
            }
        },

        watch: {
            options: { 
                livereload: true 
            },
            sass: {
                files: ['WebContent/statics/scss/*.scss'],
                tasks: ['sass', 'autoprefixer', 'copy']
            },
            html: {
                files: ['WebContent/*.html'],
                tasks: ['copy']
            },
            js: {
                files: ['WebContent/statics/js/*.js'],
                tasks: ['jshint', 'copy']
            }
        },

        jshint: {
            all: ['WebContent/statics/js/*.js']
        },


        imagemin: {
            dynamic: {    
                options: { 
                    optimizationLevel: 3,
                    svgoPlugins: [{ removeViewBox: false }],
                    use: [mozjpeg()]
                },            
                files: [{
                    expand: true,               
                    cwd: 'img/src/',                  
                    src: ['*.{png,jpg,gif}'],
                    dest: 'img/'  
                }]
            }
        },

        grunticon: {
            icons: {
                files: [{
                    expand: true, 
                    cwd: 'img/src/svgs',
                    src: ['*.svg', '*.png'],
                    dest: 'img/grunticonOutput'
                }],
                options: {
                    compressPNG: true,
                    loadersnippet: 'grunticon.loader.js',
                    defaultWidth: '128px',
                    defaultHeight: '128px'
                }
            }
        },

        copy: {
            main: {
                expand: true,
                cwd: 'WebContent/',
                src: ['**'],
                dest: 'target/shufflestream/',
            },
        }

    });

// Set Grunt tasks
grunt.registerTask('default', ['sass', 'autoprefixer', 'cssmin', 'copy']);
grunt.registerTask('full', ['sass', 'autoprefixer', 'cssmin', 'concat', 'uglify', 'imagemin', 'grunticon']);
grunt.registerTask('hint', ['jshint']);
}
