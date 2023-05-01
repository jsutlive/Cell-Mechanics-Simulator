folder = uigetdir();
fileList = dir(fullfile(folder, '*.csv'));
secondsPerSaveInterval = 60;
numberOfFiles = size(fileList);

figureTitle = strcat(get_deepest_folder_name(folder), ' maximum height');

if(numberOfFiles > 0)
    h = figure;
    hold on;
    title(figureTitle);
    names = strings([numberOfFiles, 1]);
    for n = 1:numberOfFiles
        table = readtable(strcat(folder, '\', fileList(n).name), 'Delimiter', ',');
        %max_dist = zeros(1,size(table(2)-1));
        max_dist = table2array(table(6, 2:end));
        numberOfDataPoints = size(max_dist, 2);

        max_dist_array = zeros(1, numberOfDataPoints);
        for i = 1:numberOfDataPoints
            max_dist_array(i) = str2double(max_dist{i});
        end

        t = 0:secondsPerSaveInterval:numberOfDataPoints*(secondsPerSaveInterval - 1);

        plot(t/60, max_dist_array)
        names(n) = fileList(n).name;
    end
    legend(names);
    saveas(h, strcat(figureTitle, '.jpg')); 
end