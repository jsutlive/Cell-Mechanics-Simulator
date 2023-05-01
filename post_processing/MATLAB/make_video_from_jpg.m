folder = uigetdir();
fileList = dir(fullfile(folder, '*.jpg'));
[~,idx] = sort([fileList.datenum]);
fileList = fileList(idx);

numberOfFiles = size(fileList, 1);
textOverlayPosition = [25 25];

if( numberOfFiles > 0)
    images = cell(numberOfFiles, 1);
    for i = 1 : numberOfFiles
        images{i} = imread(strcat(folder, '\', fileList(i).name));
    end
    
     % create the video writer with 1 fps
 writerObj = VideoWriter(get_deepest_folder_name(folder));
 writerObj.FrameRate = 5;
 % open the video writer
 open(writerObj);
 % write the frames to the video
 for u = 1 : numberOfFiles
     % convert the image to a frame
     frame = im2frame(images{u});
     writeVideo(writerObj, frame);
 end
 % close the writer object
 close(writerObj);
end