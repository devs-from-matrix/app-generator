const _ = require('lodash');
const path = require('path');
const gulp = require('gulp');
const gulpRename = require('gulp-rename');
const runSequence = require('gulp4-run-sequence');
const argv = require('yargs').argv;
const assertOk = require('assert-ok');
const fs = require("fs-extra");
const gulpReplace = require('gulp-replace');
const axios = require('axios');
const templates = require('read-data').sync('dfm-templates.json', {});

const config = {};

gulp.task('initialize-config', (done) => {
  if (!config.isInitialized) {
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

    const org = argv.go || argv.organization;
    config.gitHubOrganization = _.isEmpty(org) ? 'devs-from-matrix' : org;

    config.domainNameLowerCase = config.domainName.toLowerCase();
    config.domainNameCamelCase = _.camelCase(config.domainName);
    config.domainWords = _.words(config.domainName);
    config.domainGroupWords = _.concat(['org', 'dfm'], config.domainWords);
    config.domainNameStartCase = _.startCase(config.domainName).replace(' ',
        '');
    config.domainNameUpperCase = _.toUpper(_.snakeCase(config.domainWords));
    config.domainPackage = _.join(config.domainGroupWords, '.');
    config.newServicePath = _.join(config.domainGroupWords, path.sep);
    config.domainNamePlural = config.domainName.endsWith('y')
        ? `${config.domainNameCamelCase.substr(0,
            config.domainNameCamelCase.length - 1)}ies`
        : `${config.domainNameCamelCase}s`;
    config.isInitialized = true;
  }
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

gulp.task('clone-template-repo', () => {
  // Get the repoUrl from the templateName argument
  const selectedTemplate = _.find(templates, template => {
    return template.name === config.templateName;
  });
  if (!selectedTemplate) {
    throw `unknown template: ${config.templateName}`;
  }
  return require('simple-git/promise')(path.join(__dirname, 'templates'))
  .clone(selectedTemplate.url);
});

gulp.task('generate-template', gulp.series('clone-template-repo', (resolve) => {
  // Delete files mentioned in .dfm-generator ignoreFiles
  const templateConfig = require('read-data').sync(
      `templates/${config.templateName}/.dfm-generator.yml`, {});
  fs.removeSync(`templates/${config.templateName}/.git`);
  if (templateConfig.ignoreFiles) {
    templateConfig.ignoreFiles.forEach(ignore => {
      fs.removeSync(`templates/${config.templateName}/${ignore}`);
      fs.removeSync(`templates/${config.templateName}/.dfm-generator.yml`);
    });
  }
  resolve();
}));

gulp.task('generate', gulp.series('generate-template', () => {
  // Ensure your have the generated directory
  fs.ensureDir("generated");

  // Interpolate
  const parsePackageName = gulpReplace('packagename', config.domainPackage);
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
    replaceName(file, 'packagename', config.newServicePath);
    replaceName(file, 'Example', config.domainNameStartCase);
    replaceName(file, 'example', config.domainNameLowerCase);
  });

  const replaceName = function (file, oldName, newName) {
    if (_.includes(file.basename, oldName)) {
      file.basename = _.replace(file.basename, oldName, newName);
    }
    if (_.includes(file.dirname, oldName)) {
      file.dirname = _.replace(file.dirname, oldName, newName);
    }
  };

  const pathSrc = [
    `${__dirname}/templates/${config.templateName}/**`
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
}));

gulp.task('clone-target', () => {
  return require('simple-git/promise')(path.join(__dirname, 'git-generated'))
  .clone(
      `https://${config.gitHubOrganization}:${config.gitHubToken}@github.com/${config.gitHubOrganization}/${config.domainNameLowerCase}`);
});

gulp.task('commit_and_push', () => {
  function copy() {
    return fs.copySync(path.join(__dirname, 'generated'),
        path.join(__dirname, 'git-generated', config.domainNameLowerCase));
  }

  function commit() {
    return new Promise(resolve => {
      require('simple-git')(
          path.join(__dirname, 'git-generated', config.domainNameLowerCase))
      .add('./*')
      .commit('feat: initial commit from devs-from-matrix/app-generator')
      .push('origin', 'master', resolve);
    });
  }

  return Promise.all([
    copy(), commit()
  ]);
});

gulp.task('cleanup', (done) => {
  fs.emptyDir('generated');
  fs.emptyDir('git-generated');
  fs.emptyDir('templates');
  done();
});

gulp.task('helpme', (done) => {
  console.log('gulp [--options] [value]');
  console.log(' option \toption \t\t\tdescription \t\t\t\t\t\t type');
  console.log(' --------------------------------------------------------------------------------------------------------');
  console.log(' d \t\tdomain \t\t\tThe domain name \t\t\t\t\t [string]');
  console.log(' t \t\ttemplateName \t\tThe name of the template(refer dfm-templates.json) \t [string]');
  console.log(' go \t\torganization \t\tGitHub organization where the code would scaffold \t [string]');
  console.log(' gt \t\ttoken \t\t\tGitHub token with admin access \t\t\t\t [string]');
  console.log('\n');
  done();
});


gulp.task('default', gulp.series('initialize-config', (done) => {
  runSequence('cleanup', 'create-repo', 'generate', 'clone-target',
      'commit_and_push', done);
}));
// uncomment when you want to test this locally
/*gulp.task('default', gulp.series('initialize-config', (done) => {
  runSequence('cleanup', 'generate', done);
}));*/
