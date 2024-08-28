package org.iesvdm.preproyectoapirest.service;

import org.iesvdm.preproyectoapirest.domain.Tag;
import org.iesvdm.preproyectoapirest.dto.TagDTO;
import org.iesvdm.preproyectoapirest.exception.EntityNotFoundException;
import org.iesvdm.preproyectoapirest.mapper.TagMapper;
import org.iesvdm.preproyectoapirest.repository.TagRepository;
import org.iesvdm.preproyectoapirest.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.iesvdm.preproyectoapirest.service.TaskService.getCounts;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;
    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TaskRepository taskRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
        this.tagMapper = tagMapper;
    }

    public List<TagDTO> all() {
        List<Tag> tags = tagRepository.findAll();
        List<TagDTO> tagDTOList = new ArrayList<>();
        tags.forEach(tag -> {
            TagDTO tagDTO = tagMapper.tagToTagDTO(tag);
            tagDTOList.add(tagDTO);
        });

        return tagDTOList;
    }

    public List<Tag> all(Optional<String> findOpt, Optional<String> orderOpt) {
        Sort sort = null;

        if (orderOpt.isPresent()) {
            sort = orderOpt.get().equals("desc") ? Sort.by("name").descending() : Sort.by("name").ascending();
        }

        if (findOpt.isPresent()) {
            return this.tagRepository.findTagsByNameContainingIgnoreCase(findOpt.get(), sort);
        } else {
            return sort != null ? this.tagRepository.findAll(sort) :
                    this.tagRepository.findAll();
        }
    }

    public List<TagDTO> findTagsByUserID(Optional<Long> id) {
        List<Tag> tags = new ArrayList<>();
        if (id.isPresent()) {
            List<Tag> defaulTags = this.tagRepository.findTagsByUser_id(null);
            tags.addAll(defaulTags);
            List<Tag> tagDTOList = this.tagRepository.findTagsByUser_id(id.get());
            tags.addAll(tagDTOList);
        }

        List<TagDTO> tagDTOList = new ArrayList<>();
        tags.forEach(tag -> {
            TagDTO tagDTO = tagMapper.tagToTagDTO(tag);
            tagDTOList.add(tagDTO);
        });

        return tagDTOList;
    }

    public Map<String, Object> all(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Tag> pageAll = this.tagRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("tags", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }

    public TagDTO save(Tag tag) {
        Tag tempTag = this.tagRepository.save(tag);
        return tagMapper.tagToTagDTO(tempTag);
    }

    public TagDTO one(Long id) {
        Tag tempTag = this.tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Tag.class));
        return tagMapper.tagToTagDTO(tempTag);
    }

    public TagDTO replace(Long id, Tag tag) {
        Tag tempTag = this.tagRepository.findById(id)
                .map(p -> (id.equals(tag.getId()) ? this.tagRepository.save(tag) : null))
                .orElseThrow(() -> new EntityNotFoundException(id, Tag.class));

        return tagMapper.tagToTagDTO(tempTag);
    }

    public void delete(Long id) {
        this.tagRepository.findById(id).map(p -> {
            p.getTasks().stream()
                    .filter(task -> task.getTag().getId().equals(p.getId()))
                    .forEach(task -> {
                        Optional<Tag> tagOptional = this.tagRepository.findById(1L); // siempre va a haber una tag
                        if (tagOptional.isPresent()) {
                            task.setTag(tagOptional.get());
                            this.taskRepository.save(task);
                        }
                    });
            this.tagRepository.delete(p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException(id, Tag.class));
    }

    public Map<String, Map<Integer, Long>> getTaskTagMap(String startDate, String endDate, Long userId, Long taskId) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;
        try {
            start = formatter.parse(startDate);
            end = formatter.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Object[]> results = tagRepository.findMonthlyTaskCountByTag(userId, start, end, taskId);

        return getCounts(results);
    }


    public Map<Long, Long> tasksGroupByTagIdAndYear(String startDate, String endDate, Long userId) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;
        try {
            start = formatter.parse(startDate);
            end = formatter.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Object[]> results = tagRepository.tasksGroupByTagId(userId, start, end);

        return getTaskGroupByTagId(results);
    }

    static Map<Long, Long> getTaskGroupByTagId(List<Object[]> results) {
        Map<Long, Long> tasksGrouped = new HashMap<>();

        for (Object[] result : results) {
            Long tagId = (Long) result[0];
            Long totalTask = (Long) result[1];

            tasksGrouped.put(tagId, totalTask);
        }

        return tasksGrouped;
    }

}
