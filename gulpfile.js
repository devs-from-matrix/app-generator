const _ = require('lodash');
const path = require('path');
const gulp = require('gulp');
const gulpRename = require('gulp-rename');
const runSequence = require('gulp4-run-sequence');
const argv = require('yargs').argv;
const assertOk = require('assert-ok');
const del = require('del');
const fs = require("fs-extra");
const gulpReplace = require('gulp-replace');
const axios = require('axios');

// Validate service name
const domainName = argv.d || argv.domain;
assertOk(/^([a-z][a-z0-9]+)(\-[a-z][a-z0-9]+)*$/.test(domainName),
    `Invalid domain name "${domainName}"`);

// Validate template name
const templateName = argv.t || argv.templateName;
assertOk(!_.isEmpty(templateName), 'templateName is required');

const domainNameLowerCase = domainName.toLowerCase();
const domainNameCamelCase = _.camelCase(domainName);
const domainWords = _.words(domainName);
const domainGroupWords = _.concat(['org', 'dfm'], domainWords);
const domainNameStartCase = _.startCase(domainName).replace(' ', '');
const domainNameUpperCase = _.toUpper(_.snakeCase(domainWords));
const domainPackage = _.join(domainGroupWords, '.');
const newServicePath = _.join(domainGroupWords, path.sep);
const domainNamePlural = domainName.endsWith('y')
    ? `${domainNameCamelCase.substr(0, domainNameCamelCase.length - 1)}ies`
    : `${domainNameCamelCase}s`;

// ensure your have the generated directory
fs.ensureDir("generated");

// Interpolate
const parsePackageName = gulpReplace('packageName', domainPackage);
const parseArtifactId = gulpReplace('artifactName', domainNameLowerCase);
const parsePluralLowerCase = gulpReplace(/examples/g, domainNamePlural);
const parsePluralStartCase = gulpReplace(/Examples/g, _.startCase(domainNamePlural).replace(' ', ''));
const parseStartCase = gulpReplace(/Example/g, domainNameStartCase);
const parseLowerCase = gulpReplace(/example/g, domainNameCamelCase);
const parseUpperCase = gulpReplace(/EXAMPLE/g, domainNameUpperCase);

// Update filename & file path
const renamePackage = gulpRename(function (file) {
  replaceName(file, 'packageName', newServicePath);
  replaceName(file, 'Example', domainNameStartCase);
});

const replaceName = function (file, oldName, newName) {
  if (_.includes(file.basename, oldName)) {
    file.basename = _.replace(file.basename, oldName, newName);
  }
  if (_.includes(file.dirname, oldName)) {
    file.dirname = _.replace(file.dirname, oldName, newName);
  }
};

const filesTobeIgnored = [
  `!${__dirname}/templates/${templateName}/.idea/**`,
  `!${__dirname}/templates/${templateName}/acceptance-test/target/**`,
  `!${__dirname}/templates/${templateName}/domain/target/**`,
  `!${__dirname}/templates/${templateName}/domain-api/target/**`,
  `!${__dirname}/templates/${templateName}/rest-adapter/target/**`,
  `!${__dirname}/templates/${templateName}/jpa-adapter/target/**`,
  `!${__dirname}/templates/${templateName}/bootstrap/target/**`,

  `!${__dirname}/templates/${templateName}/acceptance-test/*.iml`,
  `!${__dirname}/templates/${templateName}/domain/*.iml`,
  `!${__dirname}/templates/${templateName}/domain-api/*.iml`,
  `!${__dirname}/templates/${templateName}/rest-adapter/*.iml`,
  `!${__dirname}/templates/${templateName}/jpa-adapter/*.iml`,
  `!${__dirname}/templates/${templateName}/bootstrap/*.iml`,

  `!${__dirname}/templates/${templateName}/acceptance-test/.idea/**`,
  `!${__dirname}/templates/${templateName}/domain/.idea/**`,
  `!${__dirname}/templates/${templateName}/domain-api/.idea/**`,
  `!${__dirname}/templates/${templateName}/rest-adapter/.idea/**`,
  `!${__dirname}/templates/${templateName}/jpa-adapter/.idea/**`,
  `!${__dirname}/templates/${templateName}/bootstrap/.idea/**`,
];

const pathSrc = [
  `${__dirname}/templates/${templateName}/**`, // template path
  ...filesTobeIgnored, // do not scan intellij files,
];

/**
 * Generate sources into generated folder
 */
gulp.task('generate', () => {

  return gulp.src(pathSrc, {dot: true})
  .pipe(renamePackage)
  .pipe(parsePackageName)
  .pipe(parseArtifactId)
  .pipe(parsePluralLowerCase)
  .pipe(parsePluralStartCase)
  .pipe(parseStartCase)
  .pipe(parseLowerCase)
  .pipe(parseUpperCase)
  .pipe(gulp.dest(`./generated/`));
});

gulp.task('cleanup', () => {
  console.log(`Cleaning up the generated code ...`);
  return del([
    'generated/**/*',
  ]);
});

gulp.task('create-repo', () => {
  axios({
    method: 'post',
    url:'/basic-template-repository/generate',
    baseURL: 'https://api.github.com/repos/anupbaranwal',
    headers: {
      'Authorization': 'Bearer 5ca06fea7ad038374ab22aa72088fb4414bc12ad',
      'Accept': 'application/vnd.github.baptiste-preview+json'
    },
    data: {
      owner: 'anupbaranwal',
      name: domainNameLowerCase,
      description: 'test-repo',
      private: false
    }
  }).then(res => {
    console.log('success', res);
  }).catch(error => {
    console.log('Anup error' + error);
  });
});

gulp.task('default', function (done) {
  // runSequence('cleanup', 'generate', done);
  runSequence('cleanup', 'create-repo', 'generate', done);
});
