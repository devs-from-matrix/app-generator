const _ = require('lodash');
const path = require('path');
const gulp = require('gulp');
const gulpTemplate = require('gulp-template');
const gulpRename = require('gulp-rename');
const runSequence = require('gulp4-run-sequence');
const argv = require('yargs').argv;
const assertOk = require('assert-ok');
const moment = require('moment');
const generatorVersion = require('./package.json').version;
const del = require('del');
const fs = require("fs-extra");
const gulpReplace = require('gulp-replace');

// Validate service name
const domainName = argv.d || argv.domain;
assertOk(/^([a-z][a-z0-9]+)(\-[a-z][a-z0-9]+)*$/.test(domainName), `Invalid service name "${domainName}"`);
// Build some helper const
const domainNameLowerCase = _.lowerCase(domainName);

// Validate template name
const templateName = argv.t || argv.templateName;
assertOk(!_.isEmpty(templateName), 'templateName is required');

/**
 * Generate sources into generated folder
 */
gulp.task('generate', () => {
    console.log(`Generating ${domainNameLowerCase}...`);
    const domainWords = _.words(domainName);
    const domainGroupWords = _.concat(['com', 'dfm'], domainWords);
    const domainNameInitials = _.toUpper(_.join(_.map(domainWords, _.first), ''));
    const domainNameStartCase = _.startCase(domainName);
    const domainNameUpperCase = _.toUpper(_.snakeCase(domainWords));
    const domainPackage = _.join(domainGroupWords, '.');
    const newServicePath = _.join(domainGroupWords, path.sep);

    // ensure your have the generated directory
    fs.ensureDir("generated");

    // Interpolate
    const parse = gulpReplace('domainName', domainName);

    // Update filename & file path
    const rename = gulpRename(function (file) {
        replace(file, 'template');
    });

    const replace = function (file, name) {
        if (_.includes(file.basename, name)) {
            file.basename = _.replace(file.basename, name, newServicePath);
        }
        if (_.includes(file.dirname, name)) {
            file.dirname = _.replace(file.dirname, name, newServicePath);
        }
    };

    return gulp.src(__dirname + `/templates/${templateName}/**`, {dot: true})
        .pipe(rename)
        .pipe(parse)
        .pipe(gulp.dest(`./generated/`));
});


gulp.task('cleanup', () => {
    console.log(`Cleaning up the generated code ...`);
    return del([
        'generated/**/*',
    ]);
});

gulp.task('default', function (done) {
    runSequence('cleanup', 'generate', done);
});
