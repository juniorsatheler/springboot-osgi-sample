package com.mornati.sample.controllers;

import com.mornati.sample.commons.plugins.dto.ActionResponse;
import com.mornati.sample.commons.plugins.dto.NotificationResponse;
import com.mornati.sample.service.PluginList;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/samples")
@RequiredArgsConstructor
public class SampleController {

  private final PluginList pluginList;

  @GetMapping
  @ResponseBody
  public ResponseEntity<Set<String>> getSamples() {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(pluginList.registered());

  }

  @GetMapping(value = "/{name}")
  @ResponseBody
  public ResponseEntity<ActionResponse> doAction(@PathVariable(value = "name") String name) {
    return pluginList.lookup(name).map(pluginImpl -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(pluginImpl.doAction())).orElseGet(() -> ResponseEntity.badRequest().body(ActionResponse.builder().body("Plugin not found for " + name).build()));
  }

  @GetMapping(value = "/{name}/notification")
  @ResponseBody
  public ResponseEntity<NotificationResponse> doNotify(@PathVariable(value = "name") String name) {
    return pluginList.lookup(name).map(pluginImpl -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(pluginImpl.doNotification())).orElseGet(() -> ResponseEntity.badRequest().body(NotificationResponse.builder().body("Plugin not found for " + name).build()));
  }

}
