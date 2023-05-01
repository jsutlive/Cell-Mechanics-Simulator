function [name] = get_deepest_folder_name(directory)
    [~, name, ~] = fileparts(directory);
end