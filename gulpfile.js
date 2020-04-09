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

const config = {};

/**
 * Initialize all the configurations
 */
gulp.task('initialize-config', (done) => {
  console.log(`Initializing the config ...`);
  // Validate service name
  config.domainName = argv.d || argv.domain;
  assertOk(/^([a-z][a-z0-9]+)(\-[a-z][a-z0-9]+)*$/.test(config.domainName),
      `Invalid domain name: ${config.domainName}`);

  // Validate template name
  config.templateName = argv.t || argv.templateName;
  assertOk(!_.isEmpty(config.templateName), 'templateName is required');

  // Validate template name
  config.gitHubToken = argv.gt || argv.token;
  assertOk(!_.isEmpty(config.gitHubToken), 'token is required');
  config.gitHubOrganization = _.isEmpty(argv.go || argv.organization)
      ? 'devs-from-matrix' : argv.o || argv.organization;

  config.domainNameLowerCase = config.domainName.toLowerCase();
  config.domainNameCamelCase = _.camelCase(config.domainName);
  config.domainWords = _.words(config.domainName);
  config.domainGroupWords = _.concat(['org', 'dfm'], config.domainWords);
  config.domainNameStartCase = _.startCase(config.domainName).replace(' ', '');
  config.domainNameUpperCase = _.toUpper(_.snakeCase(config.domainWords));
  config.domainPackage = _.join(config.domainGroupWords, '.');
  config.newServicePath = _.join(config.domainGroupWords, path.sep);
  config.domainNamePlural = config.domainName.endsWith('y')
      ? `${config.domainNameCamelCase.substr(0,
          config.domainNameCamelCase.length - 1)}ies`
      : `${config.domainNameCamelCase}s`;
  done();
});

gulp.task('create-repo', () => {
  const configs = {
    headers: {
      Authorization: `Bearer ${config.gitHubToken}`,
      Accept: 'application/vnd.github.baptiste-preview+json'
    }
  };
  const repoUrl = `https://api.github.com/repos/devs-from-matrix/basic-template-repository/generate`;
  const data = {
    owner: config.gitHubOrganization,
    name: config.domainNameLowerCase,
    description: `Hexagonal spring boot service for ${config.domainNameLowerCase}`,
    private: false
  };
  return Promise.all([
    axios.post(repoUrl, data, configs)
  ]);
});

/**
 * Generate sources into generated folder
 */
gulp.task('generate', () => {
  console.log(`Generating the code from the chosen template ...`);

  // Ensure your have the generated directory
  fs.ensureDir("generated");

  // Interpolate
  const parsePackageName = gulpReplace('packageName', config.domainPackage);
  const parseArtifactId = gulpReplace('artifactName',
      config.domainNameLowerCase);
  const parsePluralLowerCase = gulpReplace(/examples/g,
      config.domainNamePlural);
  const parsePluralStartCase = gulpReplace(/Examples/g,
      _.startCase(config.domainNamePlural).replace(' ', ''));
  const parseStartCase = gulpReplace(/Example/g, config.domainNameStartCase);
  const parseLowerCase = gulpReplace(/example/g, config.domainNameCamelCase);
  const parseUpperCase = gulpReplace(/EXAMPLE/g, config.domainNameUpperCase);

  // Update filename & file path
  const renamePackage = gulpRename(function (file) {
    replaceName(file, 'packageName', config.newServicePath);
    replaceName(file, 'Example', config.domainNameStartCase);
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
    //`!${__dirname}/templates/${templateName}/.idea/**`,
  ];

  const pathSrc = [
    `${__dirname}/templates/${config.templateName}/**`, // template path
    ...filesTobeIgnored, // do not scan intellij files,
  ];
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
  return fs.emptyDir('generated');
});

gulp.task('default', function (done) {
  runSequence('cleanup', 'initialize-config', 'create-repo', 'generate', done);
});
