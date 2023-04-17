folder = uigetdir();
fileList = dir(fullfile(folder, '*.csv'));

readtable(fileList(1).name, 'Delimiter', ',');