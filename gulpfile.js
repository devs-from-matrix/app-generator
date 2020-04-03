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
assertOk(/^([a-z][a-z0-9]+)(\-[a-z][a-z0-9]+)*$/.test(domainName),
    `Invalid service name "${domainName}"`);

// Build some helper const
const domainNameLowerCase = domainName.toLowerCase();
console.log(domainNameLowerCase);

// Validate template name
const templateName = argv.t || argv.templateName;
assertOk(!_.isEmpty(templateName), 'templateName is required');

/**
 * Generate sources into generated folder
 */
gulp.task('generate', () => {
  const domainWords = _.words(domainName);
  const domainGroupWords = _.concat(['org', 'dfm'], domainWords);
  const domainNameInitials = _.toUpper(_.join(_.map(domainWords, _.first), ''));
  const domainNameStartCase = _.startCase(domainName);
  const domainNameUpperCase = _.toUpper(_.snakeCase(domainWords));
  const domainPackage = _.join(domainGroupWords, '.');
  const newServicePath = _.join(domainGroupWords, path.sep);

  // ensure your have the generated directory
  fs.ensureDir("generated");

  // Interpolate
  const parsePackageName = gulpReplace('packageName', domainPackage);
  const parseArtifactId = gulpReplace('artifactName', domainNameLowerCase);

  // Update filename & file path
  const rename = gulpRename(function (file) {
    replace(file, 'packageName');
  });

  const replace = function (file, name) {
    if (_.includes(file.basename, name)) {
      file.basename = _.replace(file.basename, name, newServicePath);
    }
    if (_.includes(file.dirname, name)) {
      file.dirname = _.replace(file.dirname, name, newServicePath);
    }
  };

  const filesTobeIgnored = [
    `!${__dirname}/templates/${templateName}/.idea/**`,
    `!${__dirname}/templates/${templateName}/acceptance-test/target/**`,
    `!${__dirname}/templates/${templateName}/domain/target/**`,
    `!${__dirname}/templates/${templateName}/domain-api/target/**`,
    `!${__dirname}/templates/${templateName}/rest-adapter/target/**`,
    `!${__dirname}/templates/${templateName}/jpa-adapter/target/**`,

    `!${__dirname}/templates/${templateName}/acceptance-test/*.iml`,
    `!${__dirname}/templates/${templateName}/domain/*.iml`,
    `!${__dirname}/templates/${templateName}/domain-api/*.iml`,
    `!${__dirname}/templates/${templateName}/rest-adapter/*.iml`,
    `!${__dirname}/templates/${templateName}/jpa-adapter/*.iml`,

    `!${__dirname}/templates/${templateName}/acceptance-test/.idea/**`,
    `!${__dirname}/templates/${templateName}/domain/.idea/**`,
    `!${__dirname}/templates/${templateName}/domain-api/.idea/**`,
    `!${__dirname}/templates/${templateName}/rest-adapter/.idea/**`,
    `!${__dirname}/templates/${templateName}/jpa-adapter/.idea/**`,
  ];

  const pathSrc = [
    `${__dirname}/templates/${templateName}/**`, // template path
    ...filesTobeIgnored, // do not scan intellij files,
  ];
  return gulp.src(pathSrc, {dot: true})
  .pipe(rename)
  .pipe(parsePackageName)
  .pipe(parseArtifactId)
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
