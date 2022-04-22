package site.LatteIs.latteIs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class JoinService {
    private final UserRepository userRepository;

}
